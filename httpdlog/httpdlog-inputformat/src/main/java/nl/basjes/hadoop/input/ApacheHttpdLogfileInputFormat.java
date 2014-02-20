/*
 * Apache HTTPD logparsing made easy
 * Copyright (C) 2013 Niels Basjes
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.basjes.hadoop.input;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.basjes.parse.apachehttpdlog.ApacheHttpdLoglineParser;
import nl.basjes.parse.core.exceptions.InvalidDisectorException;
import nl.basjes.parse.core.exceptions.MissingDisectorsException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class ApacheHttpdLogfileInputFormat extends
        FileInputFormat<LongWritable, MapWritable> {
//    private static final Logger LOG = LoggerFactory.getLogger(ApacheHttpdLogfileInputFormat.class);

    // --------------------------------------------

    public static List<String> listPossibleFields(String logformat) throws IOException, MissingDisectorsException, InvalidDisectorException {
        return new ApacheHttpdLoglineParser<ParsedRecord>(ParsedRecord.class, logformat).getPossiblePaths();
    }

    private String logformat = null;
    public String getLogformat() {
        return logformat;
    }

    public Set<String> getRequestedFields() {
        return requestedFields;
    }

    private Set<String> requestedFields = new HashSet<String>();

    public ApacheHttpdLogfileInputFormat() {
        super();
    }

    public ApacheHttpdLogfileInputFormat(String newLogformat, Collection<String> newRequestedFields) {
        super();
        logformat = newLogformat;
        requestedFields.addAll(newRequestedFields);
    }

    // --------------------------------------------

    @Override
    public RecordReader<LongWritable, MapWritable> createRecordReader(
            final InputSplit split, final TaskAttemptContext context)
        throws IOException, InterruptedException {
        return new ApacheHttpdLogfileRecordReader(getLogformat(), getRequestedFields());
    }

}