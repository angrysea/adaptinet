package org.adaptinet.sdk.server;

import java.io.File;
import java.net.InetAddress;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.adaptinet.sdk.adaptinetex.AdaptinetException;
import org.adaptinet.sdk.adaptinetex.ServerException;
import org.adaptinet.sdk.mimehandlers.MimePop_Processor;
import org.adaptinet.sdk.processoragent.ProcessorAgent;
import org.adaptinet.sdk.processoragent.ProcessorFactory;
import org.adaptinet.sdk.processoragent.ProcessorState;
import org.adaptinet.sdk.registry.ProcessorFile;
import org.adaptinet.sdk.socket.BaseSocketServer;
import org.adaptinet.sdk.socket.PropData;


public class Server extends IServer {

	static public final String TRUE = "true";
	static public final String FALSE = "false";
	
	// Configuration defines
	static protected final String PROP_ROOT_DIR = "root";
	static protected final boolean REDIRECT_OUTPUT_STREAMS = false;

	protected int clientPriority = Thread.NORM_PRIORITY;
	protected boolean bUseProxy = false;
	protected BaseSocketServer socketServer = null;
	protected BaseSocketServer adminSocketServer = null;
	protected BaseSocketServer secureSocketServer = null;
	protected ProcessorFactory processorFactory = null;
	protected boolean bRestarting = false;
	protected boolean bFinishing = false;
	protected int nPort = 8082;
	protected int nAdminPort = 0;
	protected int nSecurePort = 0;
	protected String host = "localhost";
	protected String classpath = null;
	protected String identifier = "Server";
	protected URL url = null;
	protected int nMaxClients = 0;
	protected int maxnodes = 10;
	protected int nodelevels = 4;
	protected int nMaxConnections = 0;
	protected boolean verbose = false;
	protected boolean autoconnect = false;
	protected boolean standalone = false;
	protected boolean showconsole = false;
	protected ProcessorFile processorFile = null;
	protected NetworkAgent networkAgent = null;
	protected int timeout = 0;
	protected String fileName = null;
	protected String nodefilename = null;
	protected String connectType = null;
	protected String processorfile = null;
	protected String httpRoot = null;
	protected String webRoot = null;
	protected String SMTPHost = null;
	protected String proxyAddress = null;
	protected String keyStore = null;
	protected String keyStorePass = null;
	protected String socketType = null;
	protected String socketServerClass = null;
	protected int messageCacheSize = 5000;

	protected ServerCommandLine serverCmdLine = null;
	protected Thread shutdownHookThread;
	protected MimePop_Processor pop = null;

	public Server() throws Exception {
		super();
	}

	public void initialize(String[] args) throws AdaptinetException {

		try {
			startSequence();
			loadSettings(args);
			start();
		} catch (AdaptinetException e) {
			throw e;
		} catch (Exception e) {
			ServerException serverex = new ServerException(
					AdaptinetException.SEVERITY_FATAL,
					ServerException.TCV_INITFAILDED);
			serverex.logMessage(e);
			throw serverex;
		}
	}

	public void initialize(String configFile) throws AdaptinetException {

		try {
			startSequence();
			fileName = configFile;
			if (fileName != null) {
				loadConfig();
			}
			start();
		} catch (AdaptinetException e) {
			throw e;
		} catch (Exception e) {
			ServerException serverex = new ServerException(
					AdaptinetException.SEVERITY_FATAL,
					ServerException.TCV_INITFAILDED);
			serverex.logMessage(e);
			throw serverex;
		}
	}

	public void initialize(Properties properties) throws AdaptinetException {

		try {
			startSequence();
			loadConfig(properties);
			start();
		} catch (AdaptinetException e) {
			throw e;
		} catch (Exception e) {
			ServerException serverex = new ServerException(
					AdaptinetException.SEVERITY_FATAL,
					ServerException.TCV_INITFAILDED);
			serverex.logMessage(e);
			throw serverex;
		}
	}

	public void startSequence() throws Exception {

		int start = 0;
		try {
			InetAddress iaddr = InetAddress.getLocalHost();
			host = iaddr.toString();
			if ((start = host.indexOf('/')) > -1) {
				host = host.substring(start + 1);
			}
			identifier = iaddr.getHostName();
		} catch (Exception e) {
			try {
				identifier = "localhost";
				host = InetAddress.getByName("localhost").toString();
				if ((start = host.indexOf('/')) > -1) {
					host = host.substring(start + 1);
				}
			} catch (Exception ee) {
				throw ee;
			}
		}
	}

	public void loadSettings(String[] args) throws AdaptinetException {
		
		fileName = ServerCommandLine.findConfigFile(args);
		if (fileName != null) {
			loadConfig();
		}
		ServerCommandLine.processCommandLine(args, this);
	}

	public void setProperty(String name, String property) {
		ServerInfo.properties.setProperty(name, property);
	}

	public String getProperty(String name) {
		return ServerInfo.properties.getProperty(name);
	}

	public void start() throws AdaptinetException {

		try {
			IServer.setKey();
			shutdownHookThread = new Thread() {
				public void run() {
					try {
						shutdown();
					} catch (Exception e) {
						if (ServerInfo.bVerbose) {
							e.printStackTrace();
						}
					}
				}
			};

			Runtime.getRuntime().addShutdownHook(shutdownHookThread);

			// Create the network agent and do all of the initialization
			if(!standalone) {
				networkAgent = new NetworkAgent(this, nodefilename, autoconnect,
						connectType, maxnodes, nodelevels);
			}
			initializeSocketServer();
			intializeFactory();
			processorFile.preload();
			if(!standalone) {
				networkAgent.start();
			}
			pop = MimePop_Processor.startMailReader();
			
		} catch (AdaptinetException e) {
			throw e;
		} catch (Exception e) {
			ServerException serverex = new ServerException(
					AdaptinetException.SEVERITY_FATAL,
					ServerException.TCV_INITFAILDED);
			serverex.logMessage(e);
			throw serverex;
		}
	}

	protected void initializeSocketServer() throws AdaptinetException {

		try {
			socketServer = BaseSocketServer.createInstance(socketType,
					socketServerClass);
			socketServer.initialize(this, nPort, nMaxConnections);

			AdaptinetException serverex = new AdaptinetException(
					AdaptinetException.SEVERITY_SUCCESS,
					AdaptinetException.GEN_MESSAGE);
			serverex.logMessage(ServerInfo.VERSION + " starting.");

			socketServer.start(identifier);

			if (nAdminPort > 0) {
				adminSocketServer = BaseSocketServer.createInstance("HTTP");
				adminSocketServer.initialize(this, nAdminPort, nMaxConnections);
				adminSocketServer.start(identifier + "admin");
			}

			if (nSecurePort > 0) {
				secureSocketServer = BaseSocketServer.createInstance("TLS"
						+ socketType);
				secureSocketServer.initialize(this, nSecurePort,
						nMaxConnections);
				secureSocketServer.start(identifier + "SSL");
			}
		} catch (Exception e) {
			ServerException serverex = new ServerException(
					AdaptinetException.SEVERITY_FATAL,
					ServerException.TCV_INITFAILDED);
			serverex.logMessage(e);
			throw serverex;
		}

	}

	public void intializeFactory() throws AdaptinetException {

		try {
			if (processorfile != null) {
				processorFile = new ProcessorFile(processorfile);
			} else {
				processorFile = new ProcessorFile();
			}

			processorFactory = new ProcessorFactory(classpath, verbose);
			processorFactory.initialize(this, nMaxClients);
		} catch (Exception e) {
			ServerException serverex = new ServerException(
					AdaptinetException.SEVERITY_FATAL,
					ServerException.TCV_INITFAILDED);
			serverex.logMessage(e);
			throw serverex;
		}
	}

	public boolean killRequest(String id, boolean force) {
		return processorFactory.killProcessor(id, force);
	}

	public boolean killRequest(short id, boolean force) {
		return false;
	}

	public Vector<PropData> requestList() throws AdaptinetException {

		Map<String, ProcessorState> clientList = processorFactory.getProcessorAgents();
		Vector<PropData> vec = new Vector<PropData>(32);

		if (clientList != null) {
			String[] stat_arr = { "Idle", "Busy", "Free", "Killed", "Finished" };
			Iterator<ProcessorState> it = clientList.values().iterator();

			try {
				while (it.hasNext()) {
					ProcessorState state = it.next();
					ProcessorAgent processor = state.getProcessorAgent();

					String processorName = processor.getName();
					if (processorName == null) {
						processorName = new String("no processor");
					}

					PropData props = new PropData(processorName, Integer
							.toString(state.getId()), stat_arr[state
							.getStatus()]);

					vec.add(props);
				}
			} catch (Exception e) {
				ServerException serverex = new ServerException(
						AdaptinetException.SEVERITY_FATAL,
						ServerException.TCV_INITFAILDED);
				serverex.logMessage(e);
				throw serverex;
			}
		}

		return vec;
	}

	public URL getURL() throws AdaptinetException {

		if (url == null) {
			try {
				if (nPort != 80) {
					url = new URL("http", host, nPort, "/");
				} else {
					url = new URL("http", host, "/");
				}
			} catch (Exception e) {
				ServerException serverex = new ServerException(
						AdaptinetException.SEVERITY_FATAL,
						ServerException.TCV_URLFAILDED);
				serverex.logMessage(e);
				throw serverex;
			}
		}
		return url;
	}

	public synchronized void restart() throws AdaptinetException {

		try {
			shutdown(true);
		} catch (AdaptinetException e) {
			throw e;
		}
	}

	public synchronized void shutdown() throws AdaptinetException {

		try {
			shutdown(false);
		} catch (AdaptinetException e) {
			throw e;
		}
	}

	public synchronized void shutdown(boolean restart)
			throws AdaptinetException {

		// Let the network drop out.
		try {
			networkAgent.disconnect();
		} catch (Exception e) {
		}

		boolean bAbruptShutdown = shutdownHookThread != null
				&& shutdownHookThread.isAlive();
		try {
			if (pop != null) {
				pop.stop();
			}
		} catch (Exception e) {
		}

		if (!bAbruptShutdown) {
			try {
				Runtime.getRuntime().removeShutdownHook(shutdownHookThread);
			} catch (Exception e) {
			}
		} else {
			ServerException se = new ServerException(
					ServerException.SEVERITY_WARNING,
					ServerException.TCV_ABRUPTSHUTDOWN);
			se.logMessage("It is recommended that the Server be shut down from SWS Administration pages. "
							+ "Failure to do so could result in the loss of system resources.");
		}

		bFinishing = true;
		bRestarting = restart;

		try {
			socketServer.shutdown();
			if (adminSocketServer != null) {
				adminSocketServer.shutdown();
			}
			if (secureSocketServer != null) {
				secureSocketServer.shutdown();
			}

			processorFile.closeFile();
			networkAgent.closeFile();

			try {
				socketServer.join(10000);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			if (!restart) {
				// If shutdown occurred abruptly, calling exit will cause a
				// deadlock
				if (!bAbruptShutdown) {
					System.exit(0);
				}
			}
		} catch (Exception e) {
			ServerException serverex = new ServerException(
					AdaptinetException.SEVERITY_FATAL, 999);
			e.printStackTrace();
			throw serverex;
		}
	}

	protected void cleanup(boolean restart) throws AdaptinetException {

		try {
			if (socketServer != null) {
				socketServer.shutdown();
			}
			socketServer = null;

			if (adminSocketServer != null) {
				adminSocketServer.shutdown();
			}
			adminSocketServer = null;

			if (secureSocketServer != null) {
				secureSocketServer.shutdown();
			}
			secureSocketServer = null;

			bRestarting = false;
			bFinishing = false;
		} catch (Exception e) {
			System.out.println(e.getMessage() + " In cleanup");
		}

		if (restart) {
			try {
				loadConfig();
				start();
			} catch (AdaptinetException e) {
				throw e;
			} catch (Exception e) {
				ServerException serverex = new ServerException(
						AdaptinetException.SEVERITY_FATAL, 999);
				serverex.logMessage(e);
				throw serverex;
			}
		}
	}

	public void run(Runnable runner) {
		try {
			if (runner instanceof ProcessorAgent) {
				processorFactory.run((ProcessorAgent) runner);
			}
		} catch (ClassCastException cce) { // highly unlikely...
			ServerException xse = new ServerException(
					AdaptinetException.SEVERITY_ERROR,
					AdaptinetException.GEN_TYPEMISMATCH, cce.getMessage());
			xse.logMessage();
		}
	}

	public void saveConfig() {
		try {
			Properties serverProps = getConfig();
			serverProps.store(new java.io.FileOutputStream(fileName), fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveConfig(Properties serverProps) {
		try {
			serverProps.store(new java.io.FileOutputStream(fileName), fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void loadConfig() {

		try {
			ServerInfo.properties = ServerProperties
					.getInstance("org.adaptinet.sdk.server.SimpleProperties");
			ServerInfo.properties.load(new java.io.FileInputStream(
					fileName));
			loadConfig(ServerInfo.properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void loadConfig(Properties properties) {

		try {
			String s = null;
			socketType = properties.getProperty(ServerConfig.TYPE,
					"Processor");
			if (socketType.equals("CUSTOM")) {
				socketServerClass = properties
						.getProperty(ServerConfig.SOCKETSERVERCLASS);
			} else {
				socketServerClass = null;
			}
			nMaxConnections = Integer.parseInt(properties.getProperty(
					ServerConfig.MAX_CONNECTIONS, "30"));
			nPort = Integer.parseInt(properties.getProperty(
					ServerConfig.PORT, "0"));
			nAdminPort = Integer.parseInt(properties.getProperty(
					ServerConfig.ADMINPORT, "0"));
			maxnodes = Integer.parseInt(properties.getProperty(
					ServerConfig.MAXNODES, "0"));
			nodelevels = Integer.parseInt(properties.getProperty(
					ServerConfig.LEVELS, "0"));

			nMaxClients = Integer.parseInt(properties.getProperty(
					ServerConfig.MAX_CLIENTS, "30"));
			nSecurePort = Integer.parseInt(properties.getProperty(
					ServerConfig.SECUREPORT, "1443"));

			timeout = Integer.parseInt(properties.getProperty(
					ServerConfig.CONNECTION_TIMEOUT, "0"));
			timeout *= 1000;

			classpath = properties.getProperty(ServerConfig.CLASSPATH);

			s = properties.getProperty(ServerConfig.VERBOSE);
			if (s != null && s.equals(TRUE)) {
				verbose = true;
			} else {
				verbose = false;
			}

			s = properties.getProperty(ServerConfig.AUTOCONNECT);
			if (s != null && s.equals(TRUE)) {
				autoconnect = true;
			} else {
				autoconnect = false;
			}

			s = properties.getProperty(ServerConfig.STANDALONE);
			if (s != null && s.equals(TRUE)) {
				standalone = true;
			} else {
				standalone = false;
			}

			s = properties.getProperty(ServerConfig.SHOWCONSOLE, FALSE);
			if (s != null && s.equals(TRUE)) {
				showconsole = true;
			} else {
				showconsole = false;
			}

			s = properties.getProperty(ServerConfig.USEPROXY, FALSE);
			if (s != null && s.equals(TRUE)) {
				bUseProxy = true;
			} else {
				bUseProxy = false;
			}

			httpRoot = properties.getProperty(ServerConfig.HTTP_ROOT);
			webRoot = properties.getProperty(ServerConfig.WEB_ROOT);
			nodefilename = properties.getProperty(ServerConfig.NODE_FILE);
			connectType = properties.getProperty(ServerConfig.CONNECTTYPE);
			processorfile = properties.getProperty(ServerConfig.PROCESSOR_FILE);
			webRoot = properties.getProperty(ServerConfig.WEB_ROOT);
			httpRoot = properties.getProperty(ServerConfig.HTTP_ROOT, ".");
			SMTPHost = properties.getProperty(ServerConfig.SMTPHOST,
					"localhost");
			host = properties.getProperty(ServerConfig.HOST, host);
			proxyAddress = properties.getProperty(
					ServerConfig.PROXYADDRESS, proxyAddress);

			nSecurePort = Integer.parseInt(properties.getProperty(
					ServerConfig.SECUREPORT, "0"));
			if (nSecurePort > 0) {
				keyStore = properties.getProperty(ServerConfig.KEY_STORE,
						"KeyStore");
				keyStorePass = properties.getProperty(
						ServerConfig.KEY_STORE_PASSPHRASE, "seamaster");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getSMTPHost() {
		return SMTPHost;
	}

	public Properties getConfigFromFile() {
		Properties serverProps = new Properties();

		try {
			serverProps.load(new java.io.FileInputStream(fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serverProps;
	}

	public Properties getConfig() {
		ServerInfo.properties = ServerProperties
				.getInstance("org.adaptinet.sdk.server.SimpleProperties");

		try {
			ServerInfo.properties.setProperty(ServerConfig.HTTP_ROOT,
					httpRoot);
			ServerInfo.properties.setProperty(ServerConfig.CLASSPATH,
					classpath);
			ServerInfo.properties.setProperty(ServerConfig.WEB_ROOT,
					webRoot);
			ServerInfo.properties.setProperty(ServerConfig.SMTPHOST,
					SMTPHost);
			ServerInfo.properties.setProperty(
					ServerConfig.PROXYADDRESS, proxyAddress);
			ServerInfo.properties
					.setProperty(ServerConfig.HOST, host);

			ServerInfo.properties.setProperty(
					ServerConfig.MAX_CLIENTS, Integer
							.toString(nMaxClients));
			ServerInfo.properties.setProperty(
					ServerConfig.MAX_CONNECTIONS, Integer
							.toString(nMaxConnections));
			ServerInfo.properties.setProperty(ServerConfig.MAXNODES,
					Integer.toString(maxnodes));
			ServerInfo.properties.setProperty(ServerConfig.LEVELS,
					Integer.toString(nodelevels));
			ServerInfo.properties.setProperty(ServerConfig.PORT,
					Integer.toString(nPort));
			ServerInfo.properties.setProperty(ServerConfig.TYPE,
					socketType);
			ServerInfo.properties.setProperty(
					ServerConfig.SOCKETSERVERCLASS, socketServerClass);

			if (timeout > 0) {
				ServerInfo.properties.setProperty(
						ServerConfig.CONNECTION_TIMEOUT, Integer
								.toString(timeout / 1000));
			}
			if (nSecurePort > 0) {
				ServerInfo.properties.setProperty(
						ServerConfig.SECUREPORT, Integer
								.toString(nSecurePort));
			}
			if (nAdminPort > 0) {
				ServerInfo.properties.setProperty(
						ServerConfig.ADMINPORT, Integer
								.toString(nAdminPort));
			}
			if (processorFile != null) {
				ServerInfo.properties.setProperty(
						ServerConfig.PROCESSOR_FILE, processorFile.getName());
			}
			if (networkAgent != null) {
				ServerInfo.properties.setProperty(
						ServerConfig.NODE_FILE, networkAgent.getName());
			}
			if (verbose == true) {
				ServerInfo.properties.setProperty(
						ServerConfig.VERBOSE, TRUE);
			} else {
				ServerInfo.properties.setProperty(
						ServerConfig.VERBOSE, FALSE);
			}
			if (autoconnect == true) {
				ServerInfo.properties.setProperty(
						ServerConfig.AUTOCONNECT, TRUE);
			} else {
				ServerInfo.properties.setProperty(
						ServerConfig.AUTOCONNECT, FALSE);
			}
			if (standalone == true) {
				ServerInfo.properties.setProperty(
						ServerConfig.STANDALONE, TRUE);
			} else {
				ServerInfo.properties.setProperty(
						ServerConfig.STANDALONE, FALSE);
			}
			if (bUseProxy == true) {
				ServerInfo.properties.setProperty(
						ServerConfig.USEPROXY, TRUE);
			} else {
				ServerInfo.properties.setProperty(
						ServerConfig.USEPROXY, TRUE);
			}
			if (showconsole == true) {
				ServerInfo.properties.setProperty(
						ServerConfig.SHOWCONSOLE, TRUE);
			} else {
				ServerInfo.properties.setProperty(
						ServerConfig.SHOWCONSOLE, FALSE);
			}
			if (nSecurePort > 0) {
				ServerInfo.properties.setProperty(
						ServerConfig.KEY_STORE, keyStore);
				ServerInfo.properties.setProperty(
						ServerConfig.KEY_STORE_PASSPHRASE, keyStorePass);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ServerInfo.properties;
	}

	public Object getAvailableProcessor(String name) {
		return processorFactory.getAvailableProcessor(name);
	}

	public Object getAvailableHandler(String name) {
		return null;
	}

	public Object getAvailableBroker(String name) {
		return null;
	}

	public Object getAvailableServlet() {
		return null;
	}

	public Object getAvailableServlet(String name) {
		return null;
	}

	public String getIdentifier() {
		return identifier;
	}

	public int getLocalPort() {
		return socketServer.getLocalPort();
	}

	public int getSecureLocalPort() {
		return secureSocketServer.getLocalPort();
	}

	public int getAdminLocalPort() {
		return adminSocketServer.getLocalPort();
	}

	public InetAddress getInetAddress() {
		return socketServer.getInetAddress();
	}

	public InetAddress getAdminInetAddress() {
		return adminSocketServer.getInetAddress();
	}

	public InetAddress getSecureInetAddress() {
		return secureSocketServer.getInetAddress();
	}

	final public int getConnectionTimeOut() {
		return timeout;
	}

	final public void setConnectionTimeOut(int newValue) {
		timeout = newValue;
	}

	final public String getHost() {
		return host;
	}

	final public String getProxyAddress() {
		return proxyAddress;
	}

	final public void setHost(String host) {
		this.host = host;
	}

	final public int getPort() {
		return nPort;
	}

	final public void setPort(int nPort) {
		this.nPort = nPort;
	}

	final public String getSocketType() {
		return socketType;
	}

	final public void setSocketType(String socketType) {
		this.socketType = socketType;
	}

	final public String getSocketServerClass() {
		return socketServerClass;
	}

	final public void setSocketServerClass(String socketServerClass) {
		this.socketServerClass = socketServerClass;
	}

	final public int getAdminPort() {
		return nAdminPort;
	}

	final public void setAdminPort(int nAdminPort) {
		this.nAdminPort = nAdminPort;
	}

	final public int getSecurePort() {
		return nSecurePort;
	}

	final public int getMessageCacheSize() {
		return messageCacheSize;
	}

	final public void setMessageCacheSize(int nMessageCacheSize) {
		this.messageCacheSize = nMessageCacheSize;
	}
		
	final public void setSecurePort(int nSecurePort) {
		this.nSecurePort = nSecurePort;
	}

	public Object getRegistryFile() {
		return null;
	}

	public Object getRegistryDirectory() {
		return null;
	}

	final public boolean getVerboseFlag() {
		return verbose;
	}

	final public void setVerboseFlag(boolean verbose) {
		this.verbose = verbose;
	}

	final public boolean getAutoConnectFlag() {
		return autoconnect;
	}

	final public void setAutoConnectFlag(boolean autoconnect) {
		this.autoconnect = autoconnect;
	}

	final public boolean getStandAloneFlag() {
		return standalone;
	}

	final public void setStandAloneFlag(boolean standalone) {
		this.standalone = standalone;
	}

	final public Thread getThread() {
		return null; // thread;
	}

	final public int getClientThreadPriority() {
		return clientPriority;
	}

	final public String getHTTPRoot() {
		if (httpRoot != null && !httpRoot.endsWith(File.separator)) {
			httpRoot += File.separator;
		}
		return httpRoot;
	}

	final public void setHTTPRoot(String httpRoot) {
		this.httpRoot = httpRoot;
	}

	final public String getWebRoot() {
		return this.webRoot;
	}

	final public String getClasspath() {
		return classpath;
	}

	public String getServletClasspath() {
		return null;
	}

	final public void setClasspath(String classpath) {
		this.classpath = classpath;
	}

	final public int getMaxClients() {
		return nMaxClients;
	}

	final public void setMaxClients(int nMaxClients) {
		this.nMaxClients = nMaxClients;
	}

	final public int getMaxConnections() {
		return nMaxConnections;
	}

	final public void setMaxConnections(int nMaxConnections) {
		this.nMaxConnections = nMaxConnections;
	}

	public String getXSLPath() {
		return null;
	}

	public Object getFaultTolDBMgr() {
		return null;
	}

	public final boolean usesFaultTolerance() {
		return false;
	}

	public final boolean useProxy() {
		return bUseProxy;
	}

	public Object getSetting(String name) {
		if (name.equalsIgnoreCase(ServerConfig.SHOWCONSOLE)) {
			return new Boolean(showconsole);
		}
		return null;
	}

	public Object getService(String name) {

		if (name.equalsIgnoreCase(ServerConfig.NETWORKAGENT)) {
			return networkAgent;
		} else if (name.equalsIgnoreCase(ServerConfig.NODE_FILE)) {
			return nodefilename;
		} else if (name.equalsIgnoreCase(ServerConfig.PROCESSOR_FILE)) {
			return processorFile;
		} else if (name.equalsIgnoreCase(ServerConfig.PROCESSORFACTORY)) {
			return processorFactory;
		}
		return null;
	}

	public void setLogPath(String path) {
	}

	public String getLogPath() {
		return null;
	}

	public static void main(String args[]) {

		Server server = null;

		try {
			do {
				System.out.println(args[0]);
				try {
					server = new Server();
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				}

				server.initialize(args);
				System.out.println(ServerInfo.VERSION
						+ " listening on port [" + server.nPort
						+ "] started...");
				if (server.getAdminPort() > 0) {
					System.out.println("Administration listening on port ["
							+ server.nAdminPort + "], web serving from ["
							+ server.getWebRoot() + "]...");
				}
			} while (server.bRestarting == true);
		} catch (Exception e) {
			try {
				if (server != null) {
					server.cleanup(false);
				}
			} catch (AdaptinetException ce) {
				ce.printStackTrace();
			}
			System.err.println(ServerInfo.VERSION + " is shutting down..."
					+ "Exception=[" + e.getMessage() + "]");
			System.exit(1);
		}
	}
}
