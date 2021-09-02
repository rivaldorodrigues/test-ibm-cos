DELETE FROM perfil_usuario;
DELETE FROM usuario;
DELETE FROM perfil;

INSERT INTO perfil(id, nome) VALUES (1, 'ADMINISTRADOR');
INSERT INTO perfil(id, nome) VALUES (2, 'VIZUALIZADOR');

-- Senhas: 123456
insert into usuario(id, email, nome, login, senha) values (1, 'root@logique.com.br', 'Root', 'root', '$2a$10$JuYhmMbJSsOxCcrhzhJ/auK0RgOiWraVu1N4TFYFnmyqTovtzpddq');

insert into perfil_usuario(usuario_id, perfil_id) values (1, 1);
