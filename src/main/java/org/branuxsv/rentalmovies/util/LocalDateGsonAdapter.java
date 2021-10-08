package org.branuxsv.rentalmovies.util;

import java.io.IOException;
import java.time.LocalDate;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
* Adapter class to serialize and deserialize dates data type using 
* Gson and the LocalDate type of Java 8 
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-04-11 */

public class LocalDateGsonAdapter extends TypeAdapter<LocalDate> {

	@Override
	public void write(JsonWriter jsonWriter, LocalDate localDate) throws IOException {
		 if (localDate == null) 
		 {
	         jsonWriter.nullValue();
	     } 
		 else 
			 jsonWriter.value(localDate.toString());
	}

	@Override
	public LocalDate read(JsonReader jsonReader) throws IOException {
		 if (jsonReader.peek() == JsonToken.NULL) 
		 {
	            jsonReader.nextNull();
	            return null;
	     } 
		 else 
			 return LocalDate.parse(jsonReader.nextString());
	}

}
