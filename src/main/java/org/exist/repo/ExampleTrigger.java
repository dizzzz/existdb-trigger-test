package org.exist.repo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exist.collections.Collection;
import org.exist.collections.IndexInfo;
import org.exist.collections.triggers.CollectionTrigger;
import org.exist.collections.triggers.DocumentTrigger;
import org.exist.collections.triggers.SAXTrigger;
import org.exist.collections.triggers.TriggerException;
import org.exist.dom.persistent.DocumentImpl;
import org.exist.storage.DBBroker;
import org.exist.storage.lock.Lock;
import org.exist.storage.txn.Txn;
import org.exist.xmldb.XmldbURI;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ExampleTrigger extends SAXTrigger implements DocumentTrigger, CollectionTrigger {

    private final static Logger LOG = LogManager.getLogger(ExampleTrigger.class);

    @Override
    public void beforeCreateCollection(DBBroker broker, Txn txn, XmldbURI uri) throws TriggerException {
        LOG.debug("Not implemented");
    }

    @Override
    public void afterCreateCollection(DBBroker broker, Txn txn, Collection collection) throws TriggerException {
        LOG.debug("Not implemented");
    }

    @Override
    public void beforeCopyCollection(DBBroker broker, Txn txn, Collection collection, XmldbURI newUri) throws TriggerException {
        LOG.debug("Not implemented");
    }

    @Override
    public void afterCopyCollection(DBBroker broker, Txn txn, Collection collection, XmldbURI oldUri) throws TriggerException {
        LOG.debug("Not implemented");
    }

    @Override
    public void beforeMoveCollection(DBBroker broker, Txn txn, Collection collection, XmldbURI newUri) throws TriggerException {
        LOG.debug("Not implemented");
    }

    @Override
    public void afterMoveCollection(DBBroker broker, Txn txn, Collection collection, XmldbURI oldUri) throws TriggerException {
        LOG.debug("Not implemented");
    }

    @Override
    public void beforeDeleteCollection(DBBroker broker, Txn txn, Collection collection) throws TriggerException {
        LOG.debug("Not implemented");
    }

    @Override
    public void afterDeleteCollection(DBBroker broker, Txn txn, XmldbURI uri) throws TriggerException {
        LOG.debug("Not implemented");
    }

    @Override
    public void beforeCreateDocument(DBBroker broker, Txn txn, XmldbURI uri) throws TriggerException {
        LOG.info("Create document {}", uri.toString());
    }

    @Override
    public void afterCreateDocument(DBBroker broker, Txn txn, DocumentImpl document) throws TriggerException {


        if (document.getFileURI().toString().contains("copied")) {
            LOG.info("Prevent recreation of document {}", document.getFileURI().toString());
            return;
        }

        LOG.info("Created document {}", document.getDocumentURI());


        final byte[] data = "<a>dummy data</a>".getBytes();
        final XmldbURI newDocumentURI = XmldbURI.create(document.getFileURI().toString() + "-copied.xml");


        try (final Collection collection = broker.openCollection(document.getCollection().getURI(), Lock.LockMode.WRITE_LOCK)) {

            // Stream into database
            try (final InputStream bais = new ByteArrayInputStream(data);) {
                final IndexInfo info = collection.validateXMLResource(txn, broker, newDocumentURI, new InputSource(bais));
                final DocumentImpl doc = info.getDocument();
                doc.setMimeType("application/xml");
                bais.reset();
                collection.store(txn, broker, info, new InputSource(bais));
            }

        } catch (Exception e) {
            LOG.error(e);
            throw new TriggerException(e);
        }


    }

    @Override
    public void beforeUpdateDocument(DBBroker broker, Txn txn, DocumentImpl document) throws TriggerException {
        LOG.info("Update document {}", document.getDocumentURI());
    }

    @Override
    public void afterUpdateDocument(DBBroker broker, Txn txn, DocumentImpl document) throws TriggerException {
        LOG.info("Updated document {}", document.getDocumentURI());
    }

    @Override
    public void beforeUpdateDocumentMetadata(DBBroker broker, Txn txn, DocumentImpl document) throws TriggerException {
        LOG.debug("Not implemented");
    }

    @Override
    public void afterUpdateDocumentMetadata(DBBroker broker, Txn txn, DocumentImpl document) throws TriggerException {
        LOG.debug("Not implemented");
    }

    @Override
    public void beforeCopyDocument(DBBroker broker, Txn txn, DocumentImpl document, XmldbURI newUri) throws TriggerException {
        LOG.debug("Not implemented");
    }

    @Override
    public void afterCopyDocument(DBBroker broker, Txn txn, DocumentImpl document, XmldbURI oldUri) throws TriggerException {
        LOG.debug("Not implemented");
    }

    @Override
    public void beforeMoveDocument(DBBroker broker, Txn txn, DocumentImpl document, XmldbURI newUri) throws TriggerException {
        LOG.debug("Not implemented");
    }

    @Override
    public void afterMoveDocument(DBBroker broker, Txn txn, DocumentImpl document, XmldbURI oldUri) throws TriggerException {
        LOG.debug("Not implemented");
    }

    @Override
    public void beforeDeleteDocument(DBBroker broker, Txn txn, DocumentImpl document) throws TriggerException {
        LOG.debug("Not implemented");
    }

    @Override
    public void afterDeleteDocument(DBBroker broker, Txn txn, XmldbURI uri) throws TriggerException {
        LOG.debug("Not implemented");
    }
}
