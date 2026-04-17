package net.apple70cents.holdontoeverything.utils;

//? if >=1.18 {
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//? }

/**
 * @author 70CentsApple
 */
public class LoggerUtils {
    //? if >=1.18 {
    public static Logger LOGGER;
    //? }

    public static void init(){
        //? if >=1.18 {
        LOGGER = LoggerFactory.getLogger("hold_onto_everything");
        //? }
    }
    public static void info(String s){
        //? if >=1.18 {
        LOGGER.info(s);
        //? } else {
        /*System.out.println(s);
        *///? }
    }
    public static void warn(String s){
        //? if >=1.18 {
        LOGGER.warn(s);
        //? } else {
        /*System.out.println(s);
        *///? }
    }
    public static void error(String s){
        //? if >=1.18 {
        LOGGER.error(s);
        //? } else {
        /*System.err.println(s);
        *///? }
    }
}
