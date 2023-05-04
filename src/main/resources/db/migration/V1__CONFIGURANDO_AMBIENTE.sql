create table if not exists module (
   id SERIAL PRIMARY KEY,
   description VARCHAR NOT null,
   available boolean not null default true
);

insert into public.module (description) values ('Módulo');
insert into public.module (description) values ('Autorização');

create table if not exists public.role (
   id SERIAL PRIMARY KEY,
   key VARCHAR NOT null UNIQUE,
   description VARCHAR NOT null,
   id_module SERIAL references public.module,
   available boolean not null default true
);

insert into public.role (key, description, id_module) values ('MODULE_EDIT', 'Editar', 1);
insert into public.role (key, description, id_module) values ('MODULE_OPEN', 'Abrir', 1);
insert into public.role (key, description, id_module) values ('MODULE_INSERT', 'Inserir', 1);
insert into public.role (key, description, id_module) values ('MODULE_DELET', 'Deletar', 1);
insert into public.role (key, description, id_module) values ('ROLE_EDIT', 'Editar', 1);
insert into public.role (key, description, id_module) values ('ROLE_OPEN', 'Abrir', 1);
insert into public.role (key, description, id_module) values ('ROLE_INSERT', 'Inserir', 1);
insert into public.role (key, description, id_module) values ('ROLE_DELET', 'Deletar', 1);


create table if not exists public.profile_role (
   id SERIAL PRIMARY KEY,
   id_role SERIAL references public.role,
   profile VARCHAR NOT null,
   available boolean not null default true,
   CONSTRAINT unq_profile_role UNIQUE(id_role, profile)
);

insert into public.profile_role (id_role, profile) values (1, 'admin');
insert into public.profile_role (id_role, profile) values (2, 'admin');
insert into public.profile_role (id_role, profile) values (3, 'admin');
insert into public.profile_role (id_role, profile) values (4, 'admin');
insert into public.profile_role (id_role, profile) values (5, 'admin');
insert into public.profile_role (id_role, profile) values (6, 'admin');
insert into public.profile_role (id_role, profile) values (7, 'admin');
insert into public.profile_role (id_role, profile) values (8, 'admin');


create table if not exists  parceiros  (
   id INT PRIMARY KEY,
   nome VARCHAR NOT null,
   clientid VARCHAR NOT NULL,
   clientsecret VARCHAR NOT NULL
);
create table if not exists bancos  (
   id INT PRIMARY KEY,
   nome VARCHAR NOT null,
   urlprod VARCHAR NOT NULL,
   urlHomol VARCHAR NOT NULL,
   UrlOAuthProd VARCHAR NOT NULL,
   UrlOAuthHomol VARCHAR NOT NULL
);


create table if not exists contas  (
   id SERIAL PRIMARY KEY,
   bancoid INT,
   codbco INT,
   codage INT,
   codcta INT,
   codctabco INT,
   convenio INT,
   carteira INT,
   variacao INT,
   modalidade INT,
   ultnumbol VARCHAR,
   statusapi VARCHAR (100), 
   idparceiro INT,
   

   FOREIGN KEY (idparceiro) REFERENCES parceiros (id),
   FOREIGN KEY (bancoid) REFERENCES bancos (id)

);


insert into public.bancos (id, nome, urlprod, urlHomol, UrlOAuthProd, UrlOAuthHomol) values (
    1,'Banco do Brasil S.A','https://api.hm.bb.com.br/cobrancas/v2','https://api.bb.com.br/cobrancas/v2',
   'https://oauth.bb.com.br','https://oauth.sandbox.bb.com.br'
   );

create table if not exists beneficiariofinal  (
   id SERIAL PRIMARY KEY,
   tipo_inscricao INT,
   numero_inscricao VARCHAR (100),
   nome VARCHAR
);



