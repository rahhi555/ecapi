create type role_enum as enum ('VENDOR','CUSTOMER');
comment on type role_enum is 'ユーザーテーブルの権限列挙型。販売者:VENDOR,購入者:CUSTOMER';

create table users
(
    id serial primary key,
    firstname varchar(255) not null,
    lastname varchar(255) not null,
    email varchar(500) not null,
    password varchar(500) not null,
    enabled boolean not null default false,
    role role_enum not null default 'CUSTOMER',
    unique(email, role)
);
create index idx_users_email on users(email);
create index idx_users_role on users(role);
comment on table users is 'ユーザーテーブル';
comment on sequence users_id_seq is 'ユーザーテーブルの主キーのシークエンス';
comment on column users.id is '主キー';
comment on column users.firstname is 'ユーザー名字';
comment on column users.lastname is 'ユーザー名前';
comment on column users.email is 'メールアドレス';
comment on column users.password is 'パスワード';
comment on column users.enabled is '有効化フラグ';
comment on column users.role is 'ユーザー権限';
