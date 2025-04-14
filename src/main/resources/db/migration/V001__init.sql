create table if not exists customers
(
    id               bigint generated always as identity primary key,
    ch3_ship_to_code text not null,
    chain_name       text,
    ship_to_name     text,
    constraint uniq_ch3_ship_to_code unique (ch3_ship_to_code)
);

create table if not exists products
(
    id                    bigint generated always as identity primary key,
    material_no           text not null,
    material_desc_rus     text,
    product_category_code text,
    product_category_name text,
    constraint uniq_material_no unique (material_no)
);

create table if not exists price
(
    id                     bigint generated always as identity primary key,
    chain_name             text not null,
    material_no            text not null,
    regular_price_per_unit decimal(10, 2)
);

create table if not exists actual
(
    id                 bigint generated always as identity primary key,
    ch3_ship_to_code   text,
    material_no        text not null,
    volume_units       decimal(10, 2),
    actual_sales_value decimal(10, 2),
    date               date,
    promo_flag         text,
    constraint fk_actual_customer foreign key (ch3_ship_to_code)
        REFERENCES customers(ch3_ship_to_code)

);

create index if not exists idx_actual_material_date on actual(material_no, date);
create index if not exists idx_price_material on price(material_no);
