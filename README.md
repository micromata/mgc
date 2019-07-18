[![Build Status](https://travis-ci.org/micromata/mgc.svg?branch=master)](https://travis-ci.org/micromata/mgc)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.micromata.mgc/de.micromata.mgc.rootpom/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.micromata.mgc/de.micromata.mgc.rootpom)


# About

Micromata Genome Commons is a set of Java libraries to build mostly webserver based applications.

# Build
Checkout and build with maven using the root pom.

# License
MGC is released unter the Apache 2.0 License. 
See https://www.apache.org/licenses/LICENSE-2.0.html

# Documentation and more

Please refer to the [Wiki on GitHub](https://github.com/micromata/mgc/wiki).

# Deployment to Maven central:

Refer https://github.com/micromata/Merlin/tree/master/merlin-core#general-information-of-how-to-publish-to-maven-central

Instead of running gradle, use mvn:

```mvn -Prelease clean deploy```

The maven settings file (```~/m2/settings.xml```) may look like:
````
<settings>
  <profiles>
    <profile>
      <id>ossrh</id>
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <properties>
        <gpg.executable>gpg</gpg.executable>
        <gpg.key>xxx</gpg.key>
        <gpg.passphrase>xxx</gpg.passphrase>
      </properties>
    </profile>
  </profiles>
  <servers>
    <server>
      <id>ossrh</id>
      <username>xxx</username>
      <password>xxx</password>
    </server>
  </servers>
</settings>
```
