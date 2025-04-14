package db.migration;

import com.company.entity.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.company.util.BatchUtils.insertBatch;

@Slf4j
public class V002__Insert_initial_data extends BaseJavaMigration {
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Override
    public void migrate(Context context) {
        long start = System.currentTimeMillis();
        var priceMap = parsePrice(context);
        parseProducts(context);
        var chainsMap = parseCustomers(context);
        parseActuals(context, chainsMap, priceMap);
        long end = System.currentTimeMillis();
        log.info("Upload complete for {} second.", (end - start) / 1000);
    }

    private void parseActuals(Context context,
                              HashMap<String, String> chainsMap,
                              HashMap<String, BigDecimal> priceMap) {
        try (InputStream actualsStream = getClass().getResourceAsStream("/json/Actuals.json")) {
            if (actualsStream == null) {
                throw new IOException("Actuals.json not found. ");
            }
            List<ActualT> actuals = objectMapper.readValue(actualsStream, new TypeReference<>() {
            });
            if (actuals.isEmpty()) {
                log.warn("Actual list is empty. Skipping.");
                return;
            }

            var sql = "INSERT INTO actual (ch3_ship_to_code, material_no, actual_sales_value, " +
                      "volume_units,date, promo_flag) VALUES (?, ?, ?, ?, ?, ?)";

            try (var ps = context.getConnection().prepareStatement(sql)) {

                insertBatch(actuals, 5000, ps, (actual, stmt) -> {
                    var key = chainsMap.get(actual.ch3ShipToCode()) + actual.materialNo();
                    var value = priceMap.get(key);

                    var mid = actual.actualSalesValue().divide(actual.volumeUnits(), 2, RoundingMode.HALF_UP);
                    PromoFlag promoFlag = mid.compareTo(value) < 0 ? PromoFlag.PROMO : PromoFlag.REGULAR;

                    stmt.setString(1, actual.ch3ShipToCode());
                    stmt.setString(2, actual.materialNo());
                    stmt.setBigDecimal(3, actual.actualSalesValue());
                    stmt.setBigDecimal(4, actual.volumeUnits());
                    stmt.setDate(5, Date.valueOf(actual.date()));
                    stmt.setString(6, promoFlag.name());
                }, "actual", log);
            }

        } catch (IOException ex) {
            throw new RuntimeException("JSON loading error ", ex);
        } catch (SQLException ex) {
            throw new RuntimeException("Error when inserting into db ", ex);
        }
    }

    private void parseProducts(Context context) {
        try (InputStream productsStream = getClass().getResourceAsStream("/json/Products.json")) {
            if (productsStream == null) {
                throw new IOException("Products.json not found.");
            }

            List<ProductT> products = objectMapper.readValue(productsStream, new TypeReference<>() {
            });

            if (products.isEmpty()) {
                log.warn("Products list is empty. Skipping.");
                return;
            }

            var sql = "INSERT INTO products (material_no, material_desc_rus, product_category_code, product_category_name) " +
                      "VALUES (?, ?, ?, ?)";

            try (PreparedStatement ps = context.getConnection().prepareStatement(sql)) {
                insertBatch(products, 500, ps, (product, stmt) -> {
                    stmt.setString(1, new BigDecimal(product.materialNo()).toPlainString());
                    stmt.setString(2, product.materialDescRus());
                    stmt.setString(3, product.productCategoryCode());
                    stmt.setString(4, product.productCategoryName());
                }, "product", log);
            }

        } catch (IOException ex) {
            throw new RuntimeException("JSON loading error ", ex);
        } catch (SQLException ex) {
            throw new RuntimeException("Error when inserting into db ", ex);
        }
    }

    private HashMap<String, BigDecimal> parsePrice(Context context) {
        try (InputStream priceStream = getClass().getResourceAsStream("/json/Price.json")) {
            if (priceStream == null) {
                throw new IOException("Price.json not found. ");
            }
            List<PriceT> prices = objectMapper.readValue(priceStream, new TypeReference<>() {
            });

            if (prices.isEmpty()) {
                log.warn("Price list is empty. Skipping.");
                return new HashMap<>();
            }

            var sql = "insert into price (chain_name, material_no, regular_price_per_unit) values (?, ?, ?)";

            try (var ps = context.getConnection().prepareStatement(sql)) {
                insertBatch(prices, 500, ps, (price, stmt) -> {
                    stmt.setString(1, price.chainName());
                    stmt.setString(2, price.materialNo());
                    stmt.setBigDecimal(3, price.regularPricePerUnit());
                }, "price", log);
            }

            return new HashMap<>(
                    prices.stream().collect(Collectors.toMap(
                            p -> p.chainName() + p.materialNo(),
                            p -> p.regularPricePerUnit().setScale(2, RoundingMode.HALF_UP)
                    ))
            );

        } catch (IOException ex) {
            throw new RuntimeException("JSON loading error ", ex);
        } catch (SQLException ex) {
            throw new RuntimeException("Error when inserting into db ", ex);
        }
    }

    private HashMap<String, String> parseCustomers(Context context) {
        try (InputStream customerStream = getClass().getResourceAsStream("/json/Customers.json")) {
            if (customerStream == null) {
                throw new IOException("Customers.json not found. ");
            }
            List<CustomerT> customers = objectMapper.readValue(customerStream, new TypeReference<>() {
            });

            if (customers.isEmpty()) {
                log.warn("Customers list is empty. Skipping.");
                return new HashMap<>();
            }

            var sql = "insert into customers (ch3_ship_to_code, chain_name, ship_to_name) values (?, ?, ?)";

            try (var ps = context.getConnection().prepareStatement(sql)) {

                insertBatch(customers, 500, ps, (customer, stmt) -> {
                    stmt.setString(1, customer.ch3ShipToCode());
                    stmt.setString(2, customer.chainName());
                    stmt.setString(3, customer.shipToName());
                }, "customer", log);
            }

            return new HashMap<>(
                    customers.stream()
                            .collect(Collectors.toMap(
                                    CustomerT::ch3ShipToCode, CustomerT::chainName
                            ))
            );

        } catch (IOException ex) {
            throw new RuntimeException("JSON loading error ", ex);
        } catch (SQLException ex) {
            throw new RuntimeException("Error when inserting into db ", ex);
        }
    }

    private record PriceT(
            @JsonProperty("Chain_name")
            String chainName,

            @JsonProperty("Material No")
            String materialNo,

            @JsonProperty("Regular price per unit")
            BigDecimal regularPricePerUnit
    ) {
    }

    private record ActualT(
            @JsonProperty("CH3 Ship To Code")
            String ch3ShipToCode,

            @JsonProperty("Material No")
            String materialNo,

            @JsonProperty("Volume, units")
            BigDecimal volumeUnits,

            @JsonProperty("Actual Sales Value")
            BigDecimal actualSalesValue,

            @JsonProperty("Date")
            LocalDate date

    ) {
    }

    private record CustomerT(
            @JsonProperty("CH3 Ship To Code")
            String ch3ShipToCode,

            @JsonProperty("Chain_name")
            String chainName,

            @JsonProperty("CH3 Ship To Name")
            String shipToName
    ) {
    }

    private record ProductT(
            @JsonProperty("Material_No")
            String materialNo,

            @JsonProperty("Material_Desc_RUS")
            String materialDescRus,

            @JsonProperty("L3_Product_Category_Code")
            String productCategoryCode,

            @JsonProperty("L3_Product_Category_Name")
            String productCategoryName
    ) {
    }
}