package org.edumips64.core;

import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.edumips64.core.is.*;



public class Cache {
  private static final int WAYS = 2;
  private static final int BLOCKSIZE = 16; // bytes
  private static final int CACHESIZE = 512; // bytes
  private int set_size;

  private LinkedList<Integer>[] cache_sets;
  private int[] occupied_num; // how many blocks are already in the set

  private static final Logger logger = Logger.getLogger(Cache.class.getName());



  public Cache (){
    cache_sets = new LinkedList[WAYS];
    occupied_num = new int[WAYS];
    for (int i = 0; i < WAYS; i++){
      cache_sets[i] = new LinkedList<Integer>();
      occupied_num[i] = 0;
    }
    set_size = CACHESIZE / BLOCKSIZE;
  }


  /** ReadAddress
    return true if cache hit, false if cache miss
   **/
  public boolean ReadAddress(int addr){
    logger.info("inside ReadAddress");
    int block_num = addr / BLOCKSIZE;
    int set = block_num % WAYS;

    //debug logger
    /*
    logger.info("addr = " + addr + ", block_num = " + block_num);
    logger.info("set =" + set);
    */

    if (occupied_num[set] == 0){
      return false;
    }
    else {
      if (cache_sets[set].contains(block_num)){
        return true;
      }
      else {
        return false;
      }
    }
  }

  /** FetchDataFromMem
   put the block into caches
   **/
  public void FetchDataFromMem(int addr){
    int block_num = addr / BLOCKSIZE;
    int set = block_num % WAYS;

    if (occupied_num[set] == set_size){
      cache_sets[set].remove();
      cache_sets[set].add(block_num);
    }
    else if (occupied_num[set] < set_size){
      cache_sets[set].add(block_num);
      occupied_num[set]++;
    }
    else {
      logger.info("ERROR: Cache overflowed!!");
    }
  }


  /** reset
   reset the cache to initial state
   **/
  public void reset(){
    for (int i = 0; i < WAYS; i++){
      cache_sets[i].clear();
      occupied_num[i] = 0;
    }
  }
}
