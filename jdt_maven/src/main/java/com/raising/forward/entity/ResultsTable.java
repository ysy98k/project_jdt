package com.raising.forward.entity;

import com.baosight.common.basic.entity.BaseEntity;

import java.util.UUID;

public class ResultsTable extends BaseEntity {
    private String resultId;
    private String resultTableUUID;
    private String resultsTableName;

    public ResultsTable() {

    }


    public String getResultId() {

        return resultId;
    }

    public void setResultId() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        this.resultId = uuid;
    }

    public String getResultTableUUID() {
        return resultTableUUID;
    }

    public void setResultTableUUID(String resultTableUUID) {
        this.resultTableUUID = resultTableUUID;
    }

    public String getResultsTableName() {
        return resultsTableName;
    }

    public void setResultsTableName(String resultsTableName) {
        this.resultsTableName = resultsTableName;
    }
}
