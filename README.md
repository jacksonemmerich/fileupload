# File upload in database and file system storage with Spring Boot and Java 17 

* Desenvolvi um projeto que permite o upload e download de arquivos. Para isso, utilizei duas abordagens diferentes: na primeira, salvei a imagem diretamente no banco de dados e, na segunda, salvei somente os dados no banco e o arquivo em uma pasta específica no servidor.”

* I developed a project that allows file upload and download. For this, I used two different approaches: in the first, I saved the image directly in the database and, in the second, I saved only the data in the database and the file in a specific folder on the server.” is the translation of your message to English.

![Configuration](https://i.imgur.com/N5yVJ59.png)

    1 - crie um banco de dados e configure a jdbc url;
    2 - configure o banco;
    3 - configure o password
    4 - difina o modo como o hibernate vai lidar com seus dados, pode deixar somente "update"
    5 - defina na máquina local o caminho do storage, se for em máquinas windows só tracar por C:\\<seu-storage>

OBS: o localhost nesse projeto está na porta 9191 -> http://localhost:9191/

ENDPOINTS: 
* Salvar imagen no banco:
  * http://localhost:9191/image
    * ![](https://i.imgur.com/GRGOnp8.png)
      * 1 - No Body
      * 2 - defina o valor image
      * 3 - o tipo altere para file
      * 4 - escolha o arquivo dos tipos jpeg, jpg, png ou pdf, outro tipo não será aceito, você pode alterar os tipos aceitos no projeto.
    * ![](https://i.imgur.com/JQ0XfuN.png)
      * 1 - caminho do get
      * 2 - nome do arquivo salvo no banco
  * http://localhost:9191/image/fileSystem
    * ![](https://i.imgur.com/OT9z8rq.png)
      * 1 - o endereço do endpoint agora muda para /image/fileSystem
      * 2 - defina o valor image
      * 3 - E defina como File o tipo de envio de arquivo.
      * 4 - escolha o arquivo dos tipos jpeg, jpg, png ou pdf, outro tipo não será aceito, você pode alterar os tipos aceitos no projeto.

## Fontes: 
* https://www.youtube.com/watch?v=XUL60-Ke-L8
* https://www.youtube.com/watch?v=7L1BSy5pnGo