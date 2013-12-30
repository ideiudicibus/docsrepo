package it.crinali.www.archtprot.store;

import it.crinali.www.archprot.domain.Document;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import net.sf.webdav.ITransaction;
import net.sf.webdav.IWebdavStore;
import net.sf.webdav.StoredObject;
import net.sf.webdav.exceptions.WebdavException;


public class DatabaseStore implements IWebdavStore {

	private static org.slf4j.Logger LOG = org.slf4j.LoggerFactory
			.getLogger(DatabaseStore.class);
	 private static int BUF_SIZE = 65536;
	private EntityManagerFactory emf;
	 
    private EntityManager em;
	
	
	private File _root = null;
	private List<Document> documentList=null;
    public DatabaseStore(File root) {
        _root = root;
        emf = Persistence.createEntityManagerFactory("persistenceUnit");
        em = emf.createEntityManager();
        
    }

    public void destroy() {
    	try{if(em.isOpen()){
    		em.close();
    	}}
    	catch(Exception e) {
    	e.printStackTrace();	
    	}
    	finally{
    		 em=null;
    	}
    }

    public ITransaction begin(Principal principal) throws WebdavException {
        LOG.info("DatabaseStore.begin()");
        if (!_root.exists()) {
            if (!_root.mkdirs()) {
                throw new WebdavException("root path: "
                        + _root.getAbsolutePath()
                        + " does not exist and could not be created");
            }
        }
        return null;
    }

    public void checkAuthentication(ITransaction transaction)
            throws SecurityException {
        LOG.info("DatabaseStore.checkAuthentication()");
        // do nothing

    }

    public void commit(ITransaction transaction) throws WebdavException {
        // do nothing
        LOG.info("DatabaseStore.commit()");
    }

    public void rollback(ITransaction transaction) throws WebdavException {
        // do nothing
        LOG.info("DatabaseStore.rollback()");

    }

    public void createFolder(ITransaction transaction, String uri)
            throws WebdavException {
//        LOG.info("DatabaseStore.createFolder(" + uri + ")");
//        File file = new File(_root, uri);
//        if (!file.mkdir())
//            throw new WebdavException("cannot create folder: " + uri);
    	 throw new WebdavException("cannot create folder: " + uri);
    }

    public void createResource(ITransaction transaction, String uri)
            throws WebdavException {
        LOG.info("DatabaseStore.createResource(" + uri + ")");
//       
//        File file = new File(_root, uri);
//        try {
//            if (!file.createNewFile())
//                throw new WebdavException("cannot create file: " + uri);
//        } catch (IOException e) {
//            LOG
//                    .error("DatabaseStore.createResource(" + uri
//                            + ") failed");
//            throw new WebdavException(e);
//        }
        throw new WebdavException("cannot create resource: " + uri);
    }
    /**
     * upload a new Document in staging area, document has no attributes so
     * it's inconsistent
     */
    public long setResourceContent(ITransaction transaction, String uri,
            InputStream is, String contentType, String characterEncoding)
            throws WebdavException {

//        LOG.info("DatabaseStore.setResourceContent(" + uri + ")");
//        File file = new File(_root, uri);
//        try {
//            OutputStream os = new BufferedOutputStream(new FileOutputStream(
//                    file), BUF_SIZE);
//            try {
//                int read;
//                byte[] copyBuffer = new byte[BUF_SIZE];
//
//                while ((read = is.read(copyBuffer, 0, copyBuffer.length)) != -1) {
//                    os.write(copyBuffer, 0, read);
//                }
//            } finally {
//                try {
//                    is.close();
//                } finally {
//                    os.close();
//                }
//            }
//        } catch (IOException e) {
//            LOG.error("DatabaseStore.setResourceContent(" + uri
//                    + ") failed");
//            throw new WebdavException(e);
//        }
//        long length = -1;
//
//        try {
//            length = file.length();
//        } catch (SecurityException e) {
//            LOG.error("DatabaseStore.setResourceContent(" + uri
//                    + ") failed" + "\nCan't get file.length");
//        }
//
//        return length;
    	 throw new WebdavException("cannot create folder: " + uri);
    }

    public String[] getChildrenNames(ITransaction transaction, String uri)
            throws WebdavException {
        LOG.info("DatabaseStore.getChildrenNames(" + uri + ")");
        File file = new File(_root, uri);
        String[] childrenNames = null;
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            List<String> childList = new ArrayList<String>();
            String name = null;
            for (int i = 0; i < children.length; i++) {
                name = children[i].getName();
                childList.add(name);
                LOG.info("Child " + i + ": " + name);
            }
            childrenNames = new String[childList.size()];
            childrenNames = (String[]) childList.toArray(childrenNames);
        }
        return childrenNames;
    }

    public void removeObject(ITransaction transaction, String uri)
            throws WebdavException {
//        File file = new File(_root, uri);
//        boolean success = file.delete();
//        LOG.info("DatabaseStore.removeObject(" + uri + ")=" + success);
//        if (!success) {
//            throw new WebdavException("cannot delete object: " + uri);
//        }
    	 throw new WebdavException("cannot create folder: " + uri);

    }

    public InputStream getResourceContent(ITransaction transaction, String uri)
            throws WebdavException {
        LOG.info("DatabaseStore.getResourceContent(" + uri + ")");
        File file = new File(_root, uri);

        InputStream in;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
        } catch (IOException e) {
            LOG.error("DatabaseStore.getResourceContent(" + uri
                    + ") failed");
            throw new WebdavException(e);
        }
        return in;
    }

    public long getResourceLength(ITransaction transaction, String uri)
            throws WebdavException {
        LOG.info("DatabaseStore.getResourceLength(" + uri + ")");
        File file = new File(_root, uri);
        return file.length();
    }

    public StoredObject getStoredObject(ITransaction transaction, String uri) {

        StoredObject so = null;

        File file = new File(_root, uri);
        if (file.exists()) {
            so = new StoredObject();
            so.setFolder(file.isDirectory());
            so.setLastModified(new Date(file.lastModified()));
            so.setCreationDate(new Date(file.lastModified()));
            so.setResourceLength(getResourceLength(transaction, uri));
        }

        return so;
    }

}