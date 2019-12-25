package com.conceptualGraph.model;

import com.conceptualGraph.controller.Configurator;
import com.conceptualGraph.controller.WordCheckerController;
import com.conceptualGraph.controller.WordCheckerControllerMBean;
import com.conceptualGraph.view.App;

import javax.management.*;
import java.lang.management.ManagementFactory;

public class Main {
    public static void main( String[] args )
    {
        Configurator configurator = new Configurator();


        try{
            WordCheckerControllerMBean serverStatistics = new WordCheckerController();
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = new ObjectName("Admin:type=WordCheckerController.queryLimit");
            mbs.registerMBean(serverStatistics, name);
        }catch (MBeanRegistrationException e) {
            e.printStackTrace();
        } catch (InstanceAlreadyExistsException e) {
            e.printStackTrace();
        } catch (NotCompliantMBeanException e) {
            e.printStackTrace();
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }

        new App(configurator.getProperties()).start();
    }
}
