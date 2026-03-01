//package com.phd.llm.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
//
//import com.mongodb.ConnectionString;
//import com.mongodb.MongoClientSettings;
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//
//@Configuration
//public class MongoConfig extends AbstractMongoClientConfiguration {
//
//	@Override
//	protected String getDatabaseName() {
//		return "test";
//	} 
//
//	@Override
//	public MongoClient mongoClient() {
//		ConnectionString connectionString = new ConnectionString("mongodb://192.168.1.133:27017/test"); // 172.20.10.4 ;
//																										// 192.168.1.138
//		MongoClientSettings mongoClientSettings = MongoClientSettings.builder().applyConnectionString(connectionString)
//				.build();
//
//		try {
//			return MongoClients.create(mongoClientSettings);
//		} catch (Exception e) {
//			// Captura la excepción y registra un mensaje en los logs
//			System.err.println("No se pudo conectar a MongoDB: " + e.getMessage());
//			return null; // Permite que la aplicación continúe aunque no se conecte a MongoDB
//		}
//	}
//
////    @Override
////    public Collection<String> getMappingBasePackages() {
////        return Collections.singleton("com.phd.model");
////    }
//}
