Liferay 7 / Vaadin 7.5 OSGi integration PoC
===========================================

Requirements:
-------------
- Java 8 and Maven 
- Liferay Portal 7 (latest)

To compile:
-----------

~~~
mvn package
~~~

Deploy:
-----------

portal-ext.properties

if you are using osgi-web to service resource directly from Vaadin bundles

~~~
vaadin.resources.path=/o/vaadin7
~~~

if you are servicing Vaadin resources from resources bundle.

~~~
vaadin.resources.path=/o/vaadin761
~~~

~~~
cp distribution/target/com.liferay.vaadin7.distribution-<version>.lpkg -d <replace-this-to-your-liferay7-home>/deploy 
~~~
