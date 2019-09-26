<%@ page import="com.qunar.vc.monitor.monitor.Monitor" %>
<%

        Monitor.recordSuccess("success1");
        Monitor.recordSuccess("success2","tagKey1","tagValue2");
        Monitor.recordSuccess("success3","tagKey3","tagValue4","tagKey5");
        Monitor.recordSuccess("success4","tagKey6","tagValue7","tagKey8","tagValue9");

        Monitor.recordFailure("failure1");
        Monitor.recordFailure("failure2","tagKey1","tagValue2");
        Monitor.recordFailure("failure3","tagKey3","tagValue4","tagKey5");
        Monitor.recordFailure("failure4","tagKey6","tagValue7","tagKey8","tagValue9");

        try{
            int a = 0;
            int b = 1/a;
        }catch (Exception e){
            Monitor.recordThrowableOne("throwable1",e);
            Monitor.recordThrowableOne("throwable2",e,"tagKey1","tagValue2");
            Monitor.recordThrowableOne("throwable3",e,"tagKey3","tagValue4","tagKey5");
            Monitor.recordThrowableOne("throwable4",e,"tagKey6","tagValue7","tagKey8","tagValue9");
        }

        Monitor.recordSize("size1",100);
        Monitor.recordSize("size2","tagKey1",100);
        Monitor.recordSize("size3",100,"tagKey2","tagValue3");
        Monitor.recordSize("size4",100,"tagKey4","tagValue5","tagKey6","tagValue7");

        Monitor.recordValue("value1");
        Monitor.recordValue("value2","tagKey1");
        Monitor.recordValue("value3","tagKey2","tagValue3");
        Monitor.recordValue("value4","tagKey4","tagValue5","tagKey6");
        Monitor.recordValue("value5","tagKey7","tagValue8","tagKey9","tagValue10");


%>