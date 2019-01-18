# P3A5

Repository pentru Proiectul 3 la disciplina "Inteligenta Artificiala".

## Description

Aceasta solutie prezinta un end-point pentru a procesa imagini cu text.
Dupa ce o imagine este furnizata, solutia returneaza imaginea indreptata, impreuna cu o lista de coordonate (x,y) a colturilor unui dreptunghi a carei arie (transpusa pe imaginea furnizata) reprezinta o *"Arie de interes"* (mai specigic : o anumita structura de text).

Urmatoarele structuri sunt declarate ca "arii de interes":

* Coloane
* Linii
* Paragrafe
* Note/texte marginale
* Antetul
* Note de subsol
* Cuvinte


## Presequites

Framework: [Spring](http://spring.io/)

Limbaje: [Java 8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

Componente: [Maven](https://maven.apache.org/), [Tomcat](http://tomcat.apache.org/), [OpenCV](https://opencv.org/), [Image Magic](https://imagemagick.org/script/index.php)


## Installation



#### ( FOR INTELLIJ ) Maven:

    -  https://www.mkyong.com/maven/how-to-install-maven-in-windows/
    -  Modificati in "System Variable" M2_HOME in loc de MAVEN_HOME(http://prntscr.com/lmo3nb)
    -  s-ar putea sa trebuiasca un restart dupa ce ati adaugat System Variable
    -  dupa ce ati facut astea, intrati in proiect in pom.xml (langa .gitignore) si ar trebui sa va apara in 
    dreapta jos "Import Changes" (http://prntscr.com/lmo482) . Prima data cand instalati maven o sa dureze 
    ceva timp pana trage tot ce are nevoie.
    - ca sa fii sigur ca merge maven-ul si totul s-a importat, intra intr-o clasa random si verifica ca 
    import-urile sa nu fie cu rosu (undefined).
    
#### OpenCV:
    - copy and paste fisierul opencv_java343.dll in locatia "C:\Program Files\Java\<JDK-VERSION>\bin" si redenumiti fisierul in "opencv_java342.dll"
    - pentru a verifica, rulati main() din clasa Application ( in src\main\java\com.uaic.ai\ ) DUPA ce ati schimbat
    path-ul pentru "sample.jpg" catre absolute path 
(absolute path ex: D:\facultate\InteligentaArtificiala\P3A5\src\main\resources\sample.jpg )
    
#### ImageMagick
    - Download ImageMagick from (https://imagemagick.org/download/binaries/ImageMagick-7.0.8-14-Q16-x64-dll.exe)
	- When installing be sure to check "Install legacy utilities"

## Usage

Pentru demo, recomandam sa folositi proiectele:

- [P3A5-gateway](https://github.com/wildProgrammer/P3A5-gateway)
- [P3A5-front-end](https://github.com/wildProgrammer/P3A5-frontend)

Acestea folosesc framework-ul Angular si NodeJS pentru a communica cu solutia.



###
