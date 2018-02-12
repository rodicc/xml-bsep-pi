package ftn.xmlwebservisi.firme.helpers;

import java.util.List;

import ftn.xmlwebservisi.firme.model.MT102;
import ftn.xmlwebservisi.firme.model.MT103;
import ftn.xmlwebservisi.firme.model.MT900;
import ftn.xmlwebservisi.firme.model.MT910;

public class PorukeWrapper {

	private List<MT102> mt102Poruke;
	
	private List<MT103> mt103Poruke;
	
	private List<MT910> mt910Poruke;
	
	private List<MT900> mt900Poruke;
	
	
	

	public PorukeWrapper(List<MT102> mt102Poruke, List<MT103> mt103Poruke, List<MT900> mt900Poruke,
			List<MT910> mt910Poruke) {
		super();
		this.mt102Poruke = mt102Poruke;
		this.mt103Poruke = mt103Poruke;
		this.mt910Poruke = mt910Poruke;
		this.mt900Poruke = mt900Poruke;
	}

	public List<MT102> getMt102Poruke() {
		return mt102Poruke;
	}

	public void setMt102Poruke(List<MT102> mt102Poruke) {
		this.mt102Poruke = mt102Poruke;
	}

	public List<MT103> getMt103Poruke() {
		return mt103Poruke;
	}

	public void setMt103Poruke(List<MT103> mt103Poruke) {
		this.mt103Poruke = mt103Poruke;
	}

	public List<MT910> getMt910Poruke() {
		return mt910Poruke;
	}

	public void setMt910Poruke(List<MT910> mt910Poruke) {
		this.mt910Poruke = mt910Poruke;
	}

	public List<MT900> getMt900Poruke() {
		return mt900Poruke;
	}

	public void setMt900Poruke(List<MT900> mt900Poruke) {
		this.mt900Poruke = mt900Poruke;
	}
	
	
	
	
}
