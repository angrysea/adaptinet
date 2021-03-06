package org.adaptinet.sdk.scripting.scriptcompiler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map.Entry;

import org.adaptinet.sdk.scripting.ScriptBlock;
import org.adaptinet.sdk.scripting.ScriptParser;

public class Parser
{

    public HashMap<String, ScriptBlock> parse(String scriptFile) throws Exception
    {
    	HashMap<String, ScriptBlock> blocks = new HashMap<String, ScriptBlock>();
		try {
			ScriptParser parser = new ScriptParser();
			BufferedReader in = new BufferedReader(new FileReader(scriptFile));

			StringBuffer script = new StringBuffer();
			String line = null;

			while ((line = in.readLine()) != null) {
				script.append(line);
			}
			blocks = parser.parse(script.toString());
			in.close();
			
		} catch (Exception e) {
			StringBuilder sb = new StringBuilder();
			for(Entry<String, ScriptBlock> entry : blocks.entrySet())
			{
				entry.getValue().dump(sb);
			}
			throw e;
		}
		return blocks;
    }

}