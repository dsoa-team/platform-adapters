package br.ufpe.cin.dsoa.adapter;

import java.io.IOException;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import br.ufpe.cin.dsoa.api.event.Event;
import br.ufpe.cin.dsoa.api.event.EventAdapter;
import br.ufpe.cin.dsoa.api.event.EventConsumer;
import br.ufpe.cin.dsoa.api.event.Subscription;

public class IOAdapter implements EventAdapter {

	private static Logger logger  = Logger.getLogger(IOAdapter.class.getName());
	
	public void start() {
		try {
			FileHandler handler = new FileHandler("out.log");
			logger.addHandler(handler);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String getId() {
		return "IOAdapter";
	}

	@Override
	public void exportEvent(Event event, Map<String, Object> configuration) {
		logger.info(event.toString());
		
	}

	@Override
	public void importEvent(EventConsumer arg0, Subscription arg1) {
		// TODO Auto-generated method stub
	}

}
