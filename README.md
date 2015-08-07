Liferay 7 / Vaadin 7.5 OSGi integration PoC
===========================================

Requirements:
-------------
- Java 7 and Maven 
- Liferay Portal 7 m6

To compile:
-----------

~~~
mvn package
~~~

Configure portal:
-----------

portal-ext.properties

~~~
vaadin.resources.path=/o/vaadin7
~~~

Deploy:
-----------

~~~
cp distribution/target/com.liferay.vaadin7.distribution-<version>.lpkg -d <replace-this-to-your-liferay7-home>/deploy 
~~~
