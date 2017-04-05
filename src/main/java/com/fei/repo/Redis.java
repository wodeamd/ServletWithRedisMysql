/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fei.repo;

import redis.clients.jedis.Jedis;

public class Redis {

    private Jedis getClient(){
//        Jedis connection = new Jedis("172.16.51.47", 6379);
        Jedis connection = new Jedis(System.getenv("redhost"), 6379);
        System.out.println("Connection to redis sucessfully");
        System.out.println("Server is running: "+ connection.ping());
        return connection;
    }

    public boolean Update(String Key,String Content){
        boolean result = false;
        Jedis connection = getClient();
        try{
            connection.set(Key, Content);
            result = true;
        }
        catch(Exception e){
            System.err.println(e);  
        }
        finally{
            connection.close();
            return result;
        }
    }

    public String Get(String Key){
        System.out.println("Get by name:" + Key);
        String v = null;
        Jedis connection = getClient();
        try{

            v = connection.get(Key);
            System.out.println("Get value:" + v + " By name:" + Key);
            return v;
        }
        catch(Exception e){
            System.err.println(e);

        }
        finally{
            connection.close();
            return v;
        }
    }
    
}
