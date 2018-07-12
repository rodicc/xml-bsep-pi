package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import dto.ReportDto;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class ReportService {
	private Connection DBConnection;
	
	public boolean izvodKlijenta() {
		return false;
	}
	
	public boolean spisakRacuna(String brRacunaBanke) {
		try {
			DBConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/xmldb", "root", "root");
			
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("br_racuna", brRacunaBanke);
			
			JasperPrint jp = JasperFillManager.fillReport(getClass().getResource("/SpisakRacuna.jasper").openStream(), params, DBConnection);
			JasperExportManager.exportReportToPdfFile(jp, "./spisak_racuna_"+brRacunaBanke+".pdf");
			
			DBConnection.close();
			return true;
		}catch (Exception ex) {
			ex.printStackTrace();
			
		}
		return false;
	}

	public boolean izvodKlijenta(ReportDto dto) {
		try {
			DBConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/xmldb", "root", "root");
			
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("br_racuna", dto.getBr_racuna());
			params.put("beginDate", dto.getBeginDate());
			params.put("endDate", dto.getEndDate());
			
			JasperPrint jp = JasperFillManager.fillReport(getClass().getResource("/IzvodKlijenta.jasper").openStream(), params, DBConnection);
			JasperExportManager.exportReportToPdfFile(jp, "./spisak_racuna_"+dto.getBr_racuna()+".pdf");
			
			DBConnection.close();
			return true;
		}catch (Exception ex) {
			ex.printStackTrace();
			
		}
		return false;
	}
	
	
}
