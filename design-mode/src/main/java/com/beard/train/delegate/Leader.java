package com.beard.train.delegate;

import java.util.HashMap;
import java.util.Map;

public class Leader implements IEmployee {

    private Map<String, IEmployee> employees = new HashMap<>();

    public Leader() {
        employees.put("爬虫", new EmployeeA());
        employees.put("海报", new EmployeeB());
    }

    @Override
    public void doing(String task) {
        if (!employees.containsKey(task)) {
            System.out.println(task + "超出我能力范围");
            return;
        }
        employees.get(task).doing(task);
    }
}
