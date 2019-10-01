# Howto

When the following `collection.xconf` document is stored in `/db/system/config/db/mycollection`,
then in case a document is created in `/db/mycollection` a second document ending with `-copied.xml` is created.

Purpose: to show that the Trigger is fired, inside the eXist-db xqsuite  tests.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<collection xmlns="http://exist-db.org/collection-config/1.0">

    <triggers>
        <trigger class="org.exist.repo.ExampleTrigger"/>
    </triggers>

</collection>
```
