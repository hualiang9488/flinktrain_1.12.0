package com.guanyq.study.TableAPIAndSQL.Functions;


import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

import static org.apache.flink.table.api.Expressions.row;

public class ValueConstructionFunctions {
    public static void main(String[] args) throws Exception {
        // get env
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);
        EnvironmentSettings bsSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
        // get StreamTableEnvironment.
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env, bsSettings);

        Table table = tableEnv.fromValues(
                DataTypes.ROW(
                        DataTypes.FIELD("id", DataTypes.DECIMAL(10, 2)),
                        DataTypes.FIELD("name", DataTypes.STRING())
                ),
                row(1, "Join"),
                row(2, "Tomcat"),
                row(3, "Money"),
                row(4, "Tom")
        );

        tableEnv.createTemporaryView("Persons", table);
        Table sqlQuery = tableEnv.sqlQuery("SELECT id,ROW('a' ,'b') FROM Persons");

        tableEnv.toRetractStream(sqlQuery, Row.class).print();
        env.execute("Go");
    }
}
