package br.udesc.auxiliar;

import br.udesc.modelo.Projeto;
import br.udesc.modelo.Usuario;
import br.udesc.modelo.VariavelEntrada;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class CriaBanco {
	public static void main(String args[]) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:8889/", "root", "root");
		Statement st = con.createStatement();
                st.execute("drop database if exists fuzzy");
		st.execute("create database fuzzy");
		
		AnnotationConfiguration an = new AnnotationConfiguration();
                an.addAnnotatedClass(Projeto.class);
                an.addAnnotatedClass(Usuario.class);
                an.addAnnotatedClass(VariavelEntrada.class);

		an.configure();
		
		new SchemaExport(an).create(true, true);		
        st.close();
		con.close();
	}
}