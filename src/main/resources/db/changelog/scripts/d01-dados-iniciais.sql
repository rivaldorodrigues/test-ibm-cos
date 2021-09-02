insert into perfil(id, nome) values (1, 'ADMINISTRADOR');
insert into perfil(id, nome) values (2, 'VIZUALIZADOR');

alter sequence perfil_seq restart with 3;

--- Senha: 123456
insert into usuario(id, email, nome, login, senha) values (1, 'root@logique.com.br', 'Root', 'root', '$2a$10$JuYhmMbJSsOxCcrhzhJ/auK0RgOiWraVu1N4TFYFnmyqTovtzpddq');

insert into perfil_usuario(usuario_id, perfil_id) values (1, 1);

alter sequence usuario_seq restart with 2;
