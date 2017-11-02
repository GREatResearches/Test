package cfmts.ts.utilities;

import java.util.ArrayList;
import java.util.HashMap;

import fmp.contextEvolModel.ContextState;

public class HashMapGenerator {

	public HashMap<String, Boolean> generateMapFromContextState (ContextState ctxState){
		HashMap<String, Boolean> map = new HashMap<String, Boolean>();
		for (String	ctx : ctxState.getStringListOfAtiveContext()) {
			map.put(ctx, true);
		}
		return map;
	}
	
	public HashMap<String, Boolean> generateMapFromFeaturesState (ArrayList<String> activeFeatures){
		HashMap<String, Boolean> map = new HashMap<String, Boolean>();
		for (String	ctx : activeFeatures) {
			map.put(ctx, true);
		}
		return map;
	}
	
}
