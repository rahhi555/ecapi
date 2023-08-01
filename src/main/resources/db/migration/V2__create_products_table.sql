create table products
(
    id serial primary key,
    vendor_id int references users(id) not null,
    name text not null,
    description text,
    thumbnail_url text,
    carousel_urls TEXT[],
    price int not null check (price >= 0),
    stock int not null check (stock >= 0),
    created_at timestamptz not null default current_timestamp,
    updated_at timestamptz not null default current_timestamp
);
comment on table products is '商品テーブル';
comment on sequence products_id_seq is '商品テーブルの主キーのシークエンス';
comment on column products.id is '商品主キー';
comment on column products.vendor_id is '商品出品者の主キー。usersテーブルのroleがVENDORのみ許容';
comment on column products.name is '商品名';
comment on column products.description is '商品説明';
comment on column products.thumbnail_url is '商品サムネイル画像URL';
comment on column products.carousel_urls is '商品カルーセル画像URL配列';
comment on column products.price is '商品値段';
comment on column products.stock is '在庫';
comment on column products.created_at is '作成日時';
comment on column products.updated_at is '更新日時';

