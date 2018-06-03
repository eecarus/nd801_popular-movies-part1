package com.example.android.popularmovies.moviedb;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import org.joda.time.LocalDate;
import org.joda.time.format.ISODateTimeFormat;

import java.io.IOException;

/**
 * This is a GSON TypeAdapter that can be used to convvert a LocalDate to the YYYY-MM-DD ISO-8601
 * format instead of the normal result (which is a separate year, month, and date fields in an
 * array).
 * <p>
 * Because I wanted to maintain a minimum of API 21, I did not use DateTimeFormatter from java.time.
 */
class LocalDateTypeAdapter extends TypeAdapter<LocalDate> {

    private final String ISO_8601_FORMAT = "yyyy-MM-dd";

    @Override
    public void write(JsonWriter out, LocalDate value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.toString(ISODateTimeFormat.date()));
        }
    }

    @Override
    public LocalDate read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        String possibleDate = in.nextString();
        return LocalDate.parse(possibleDate, ISODateTimeFormat.date());
    }

}