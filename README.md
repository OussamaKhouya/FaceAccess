# FaceAccess : Facial recognition application for access management

### How to setup FaceAccess in intelliJ

1. download [jdk-23](https://drive.google.com/drive/folders/18Q75WHKDwyVfDDyaA_oiUFb2nZifTjdl?usp=sharing) and unzip file in C:\
2. download javafx-sdk-21 and unzip file in C:\
3. in intelliJ > File > Project Structure > Project > SDK > Add JDK...
4. in intelliJ > File > Project Structure > Libraries > + > Java > choose Folder: FaceAccess\lib
5. run your Application > Edit Configurations... > Modify options > Add VM options > add
````txt
--module-path "C:\javafx-sdk-21.0.5\lib" --add-modules javafx.controls,javafx.fxml
````