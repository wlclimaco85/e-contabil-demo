create table if not exists  servicos  (
   codserv SERIAL PRIMARY KEY,
   nome_servico VARCHAR(255),
   valor FLOAT
);

create table if not exists  termos  (
   versao SERIAL PRIMARY KEY,
   html_termo TEXT,
   dh_created_at TIMESTAMP,
   codserv INT,

   FOREIGN KEY (codserv) REFERENCES servicos (codserv)
);

create table if not exists  contratos  (
   codcontrato SERIAL PRIMARY KEY,
   idapibanco INT,
   idparceiro INT,
   codserv INT,
   valor_contratado FLOAT,
   dh_created_at TIMESTAMP,
   dh_updated_at TIMESTAMP,
   usu_contratante VARCHAR(255),
   versao_aceite INT,
   ativo VARCHAR(1),

   FOREIGN KEY (idapibanco) REFERENCES contas (id),
   FOREIGN KEY (idparceiro) REFERENCES parceiros (id),
   FOREIGN KEY (codserv) REFERENCES servicos (codserv),
   FOREIGN KEY (versao_aceite) REFERENCES termos (versao)
);

INSERT INTO servicos (nome_servico, valor)
VALUES('Boleto Rápido por API - Banco do Brasil', 0.04);

INSERT INTO servicos (nome_servico, valor)
VALUES('Boleto Rápido por API - Banco Itaú', 0.10);

INSERT INTO termos (html_termo, dh_created_at, codserv)
VALUES('<p dir="ltr" style="text-align:center"><strong>Contrato de licen&ccedil;a de uso do servi&ccedil;o Boleto R&aacute;pido por API</strong></p>
<p><br />
&nbsp;</p>
<p dir="ltr"><strong>Boleto r&aacute;pido por API</strong><span style="background-color:transparent; color:rgb(0, 0, 0); font-family:arial; font-size:11pt"> &eacute; um servi&ccedil;o prestado pela Sankhya em que a comunica&ccedil;&atilde;o com o banco para o registro, altera&ccedil;&atilde;o, cancelamento e retorno de boletos &eacute; feita de maneira totalmente autom&aacute;tica pela Solu&ccedil;&atilde;o Sankhya.</span></p>
<p>&nbsp;</p>
<p dir="ltr"><span style="background-color:transparent; color:rgb(0, 0, 0); font-family:arial; font-size:11pt">Assim, n&atilde;o h&aacute; necessidade de opera&ccedil;&atilde;o manual de troca de arquivos com o banco, ou de contrata&ccedil;&atilde;o de Vans ou empresas terceiras que automatizam este processo.</span></p>
<p>&nbsp;</p>
<p dir="ltr"><span style="background-color:transparent; color:rgb(0, 0, 0); font-family:arial; font-size:11pt">Para o </span><strong>Banco Ita&uacute;</strong><span style="background-color:transparent; color:rgb(0, 0, 0); font-family:arial; font-size:11pt">, este servi&ccedil;o possui o custo de </span><strong>R$ 0,10</strong><span style="background-color:transparent; color:rgb(0, 0, 0); font-family:arial; font-size:11pt"> por boleto liquidado, com fatura m&iacute;nima de </span><strong>R$ 50,00</strong><span style="background-color:transparent; color:rgb(0, 0, 0); font-family:arial; font-size:11pt"> por conta ativa.</span></p>
<p>&nbsp;</p>
<p dir="ltr"><span style="background-color:transparent; color:rgb(0, 0, 0); font-family:arial; font-size:11pt">Estes valores ser&atilde;o faturados pela Sankhya em fatura espec&iacute;fica de presta&ccedil;&atilde;o de servi&ccedil;os financeiros a ser encaminhada pelos mesmos canais em que ocorre a cobran&ccedil;a da mensalidade referente ao uso do ERP.</span></p>
<p>&nbsp;</p>
<p dir="ltr"><span style="background-color:transparent; color:rgb(0, 0, 0); font-family:arial; font-size:11pt">Esta contrata&ccedil;&atilde;o inclui a utiliza&ccedil;&atilde;o de boleto tradicional via API e tamb&eacute;m do </span><strong>Bolecode</strong><span style="background-color:transparent; color:rgb(0, 0, 0); font-family:arial; font-size:11pt">, que &eacute; a solu&ccedil;&atilde;o h&iacute;brida do Ita&uacute; BBA que permite a emiss&atilde;o de boletos com o C&oacute;digo de Barras e QR Code Pix. *Bolecode ser&aacute; disponibilizado em breve em uma nova vers&atilde;o do Sankhya OM.</span></p>
<p>&nbsp;</p>
<ul>
	<li dir="ltr">
	<p dir="ltr"><span style="background-color:transparent; font-size:11pt">A cobran&ccedil;a ocorrer&aacute; apenas pela utiliza&ccedil;&atilde;o em </span><strong>ambiente de produ&ccedil;&atilde;o</strong><span style="background-color:transparent; font-size:11pt"> da solu&ccedil;&atilde;o Sankhya. Portanto, a experimenta&ccedil;&atilde;o em ambiente de testes se d&aacute; de maneira gratuita.</span></p>
	</li>
</ul>
<p>&nbsp;</p>
<ul>
	<li dir="ltr">
	<p dir="ltr"><span style="background-color:transparent; font-size:11pt">Este servi&ccedil;o n&atilde;o possui influ&ecirc;ncia da tarifa j&aacute; negociada e paga diretamente ao banco referente a utiliza&ccedil;&atilde;o de boletos.</span></p>
	</li>
</ul>
<p>&nbsp;</p>
<p dir="ltr"><span style="background-color:transparent; color:rgb(0, 0, 0); font-family:arial; font-size:11pt">--------------------------------------------------</span></p>
<p dir="ltr"><span style="background-color:transparent; color:rgb(0, 0, 0); font-family:arial; font-size:11pt">Este servi&ccedil;o faz parte do </span><strong>Sankhya Financial Services</strong><span style="background-color:transparent; color:rgb(0, 0, 0); font-family:arial; font-size:11pt">, que &eacute; a plataforma de servi&ccedil;os financeiros da Sankhya que ir&aacute; disponibilizar diversos servi&ccedil;os, diretamente pelo EIP, de maneira totalmente integrada e segura para atender &agrave;s necessidades dos clientes Sankhya.</span></p>
<p>&nbsp;</p>
<p dir="ltr"><span style="background-color:transparent; color:rgb(0, 0, 0); font-family:arial; font-size:11pt">Em breve, novos servi&ccedil;os ser&atilde;o disponibilizados para apoiar a evolu&ccedil;&atilde;o da sua gest&atilde;o financeira com o que h&aacute; de melhor no mercado.</span></p>
<div>&nbsp;</div>', current_date, 2);
