# DEVINETOR

<!-- PROJECT LOGO -->
<br />
<p align="center">
  <a href="https://github.com/rmonier/devinetor">
    <img src="devinetor/src/main/resources/img/icones/icone.png" alt="Logo" width="130">
  </a>

<h3 align="center"><a href="https://github.com/rmonier/devinetor"><img src="devinetor/src/main/resources/img/backgrounds/titre.png" alt="devinetor" width="380"></a></h3>

  <p align="center">
    Jeu type Akinator en plus léger
    <br />
    <a href="https://github.com/rmonier/devinetor/releases"><strong>Voir les Releases »</strong></a>
    <br />
    <br />
    <a href="https://github.com/rmonier/devinetor/wiki">Lire le wiki</a>
    ·
    <a href="https://github.com/rmonier/devinetor/issues">Issues</a>
    ·
    <a href="https://github.com/rmonier/devinetor/projects">Voir le Projet</a>
  </p>

<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary>Table des Matières</summary>
  <ol>
    <li>
      <a href="#à-propos-du-projet">À Propos du Projet</a>
      <ul>
        <li><a href="#créé-avec">Créé avec</a></li>
      </ul>
    </li>
    <li>
      <a href="#commencer">Commencer</a>
      <ul>
        <li><a href="#prérequis">Prérequis</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#utilisation">Utilisation</a></li>
      <ul>
        <li><a href="#exécution">Exécution</a></li>
        <li><a href="#génération">Génération</a></li>
        <ul>
           <li><a href="#documentation">Documentation</a></li>
           <li><a href="#archive-jar">Archive JAR</a></li>
           <li><a href="#exécutable-optionnel">Exécutable (optionnel)</a></li>
        </ul>
      </ul>
    <li><a href="#arborescence">Arborescence</a></li>
    <li><a href="#crédits">Crédits</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>

***

<!-- ABOUT THE PROJECT -->
## À Propos du Projet
Jeu réalisé en 2017/2018 avec Alexandre Leblond, Mathieu Laparra et Emilien Vinet pour le projet final de Java en 2ème année INSA Lyon. Ajout en 2021 sur GitHub avec un README et une transformation en projet Gradle avec gestion automatique des dépendances.

### Créé avec
* [Java OpenJDK](https://openjdk.java.net/)
* [SQLite](https://sqlite.org/)

<!-- GETTING STARTED -->
## Commencer

### Prérequis

* Installer une version 16 d'un Java OpenJDK.

> :warning: **Pour pouvoir créer un exécutable localement (optionnel)** : D'autres programmes devront être installés manuellement selon l'OS cible (voir <a href="#exécutable-optionnel">Exécutable (optionnel)</a>). De plus, une version 64 bits de Java sera requise pour éviter un bug de `jpackage`.



### Installation

1. Cloner le dépôt sur votre machine
   ```sh
   git clone https://github.com/rmonier/devinetor.git
   ```
2. Devinetor est maintenant prêt à être utilisé.

<!-- USAGE EXAMPLES -->
## Utilisation

### Exécution

Utiliser le script suivant à la racine du projet pour lancer Devinetor :
  ```sh
  gradlew run
  ```

### Génération

Pour générer la Javadoc et l'archive JAR, lancer le script suivant :
  ```sh
  gradlew build
  ```

#### Documentation

Lors du build Devinetor utilise un script Gradle pour générer la Javadoc qui peut se retrouver à l'emplacement suivant : `devinetor/build/docs`.
Le script peut être lancé séparemment du build :
  ```sh
  gradlew javadoc
  ```

#### Archive JAR

Est aussi utilisé un script Gradle lors du build pour générer l'archive JAR qui peut se retrouver à l'emplacement suivant : `devinetor/build/libs`.
Le script peut être lancé séparemment du build :
  ```sh
  gradlew jar
  ```
Cette archive est indépendante et peut être distribuée sur tout système qui utilise un Java OpenJDK 16 en utilisant la commande suivante :
  ```sh
  java -jar devinetor-1.0.0.jar
  ```

#### Exécutable (optionnel)

Enfin un dernier script Gradle peut être utilisé après le build pour créer un exécutable compatible avec l'OS utilisé (à l'aide de l'outil `jpackage`) que l'on retrouvera à cet emplacement : `devinetor/build/dist`.
Le script à lancer est le suivant :
  ```sh
  gradlew jpackage
  ```

> :warning: **Pour mener à bien cette étape il faut posséder les programmes suivants selon l'OS utilisé** :
> * Linux: deb, rpm:
>    * For Red Hat Linux, the rpm-build package is required.
>    * For Ubuntu Linux, the fakeroot package is required.
> * macOS: pkg, app in a dmg
>    * Xcode command line tools are required when the --mac-sign option is used to request that the package be signed, and when the --icon option is used to customize the DMG image.
> * Windows: exe, msi
>    * WiX 3.0 or later is required.

***

<!-- TREE STRUCTURE -->
## Arborescence
<details>

_TODO_

</details>

<!-- CREDITS -->
## Crédits

Romain Monier [ [GitHub](https://github.com/rmonier) ] – Co-développeur
<br>
Alexandre Leblond – Co-développeur
<br>
Mathieu Laparra – Co-développeur
<br>
Emilien Vinet – Co-développeur

<!-- CONTACT -->
## Contact

Lien du Projet : [https://github.com/rmonier/devinetor](https://github.com/rmonier/devinetor)
