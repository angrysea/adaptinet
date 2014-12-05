package org.adaptinet.sdk.samples.scripttest;

import org.adaptinet.sdk.scripting.BaseRunner;
import org.adaptinet.sdk.scripting.ScriptEngine;


public class TestRunner extends BaseRunner {

    Portfolio portfolio = new Portfolio();
        
    public final Portfolio getPortfolio() {
		return portfolio;
	}

	public final void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}

    public static void main(String[] args) {
        ScriptEngine xscriptEngine = new ScriptEngine();
        try {
			xscriptEngine.loadScript(args[0]);
	        TestRunner runner = new TestRunner();
	        Object ret = xscriptEngine.execute(runner, "checkSymbol");
	        System.out.println(ret.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public void trace(String msg) {
    	System.out.println(msg);
	}

	public void debug(String msg) {
    	System.out.println(msg);
	}

	public void info(String msg) {
    	System.out.println(msg);
	}

	public void error(String msg) {
    	System.out.println(msg);
	}

	public void fatal(String msg) {
    	System.out.println(msg);
	}
}
