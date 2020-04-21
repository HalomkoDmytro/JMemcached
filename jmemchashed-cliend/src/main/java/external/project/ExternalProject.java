package external.project;

import net.devstudy.jmemcashed.exception.JMemcashedException;
import net.devstudy.jmemchashed.client.Client;
import net.devstudy.jmemchashed.client.impl.JMemchashedFactory;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class ExternalProject {

    public static void main(String[] args) throws Exception{
        try (Client client = JMemchashedFactory.buildNewClient("localhost", 9010)) {
            client.put("test", "Hello world");
            System.out.println(client.get("test"));
            client.remove("test");

            client.put("test", "Hello world");
            client.put("test", new BussinesObject("Test"));
            System.out.println(client.get("test"));
            client.clear();
            System.out.println(client.get("test"));

            client.put("devstudy", "Destudy JMemchased", 2, TimeUnit.SECONDS);
            TimeUnit.SECONDS.sleep(3);
            System.out.println(client.get("devstudy"));

        }
    }

    private static class BussinesObject implements Serializable {
        private String name;

        public BussinesObject(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "BussinesObject{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
