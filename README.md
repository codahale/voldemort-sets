Voldemort Sets
==============

voldemort-sets is a collection of view classes which allow for the efficient
storage of sets of numbers in the [Voldemort](http://project-voldemort.com/)
distributed key/value store.

Requirements
------------

voldemort-sets *requires* Voldemort 0.60.


Installation
------------

Place the provided JAR file (available in the
[downloads](http://github.com/codahale/voldemort-sets/downloads) section) in
the `lib` directory of your Voldemort install on each node.

Reboot your cluster.


Configuration
-------------

Add a view to your `stores.xml` configuration file:

    <view>
      <name>sets</name>
      <view-of>raw-sets</view-of>
      <view-class>
        com.codahale.voldemort.sets.LongSet
      </view-class>
      <value-serializer>
        <type>string</type>
      </value-serializer>
    </view>

The `<name>` element determines how your client will interface with the view.

The `<view-of>` element is the name of a defined store in which the serialized
sets will be stored. The backing store's `<value-serializer>` should be
`identity`, as the sets are stored in a compressed binary format.

The `<view-class>` element can refer to one of the following:

* `com.codahale.voldemort.sets.LongSet`: An unordered set of 64-bit integers.
  The integers are stored using [Avro's variable-length zig-zag](http://hadoop.apache.org/avro/docs/current/spec.html#binary_encode_primitive)
  which reduces the amount of disk space used and the amount of disk I/O during
  reading and writing.
* `com.codahale.voldemort.sets.SortedLongSet`: A set of 64-bit integers, stored
  in ascending order using the same storage scheme as `LongSet`.
* `com.codahale.voldemort.sets.ReverseSortedLongSet`: A set of 64-bit integers,
  stored in descending order using the same storage scheme as `LongSet`.
* `com.codahale.voldemort.sets.DoubleSet`: An unordered set of 64-bit
  floating-point numbers. The numbers are stored as fixed-length, 8-byte blocks.
* `com.codahale.voldemort.sets.SortedDoubleSet`: A set of 64-bit floating-point
  numbers, stored in ascending order using the same storage scheme as `DoubleSet`.
* `com.codahale.voldemort.sets.ReverseSortedDoubleSet`: A set of 64-bit
  floating-point numbers, stored in descending order using the same storage
  scheme as `DoubleSet`.


The `<value-serializer>`'s `<type>` element should be `string`.


Usage
-----

    import voldemort.client.*;
    
    public class SetOperationsExample {
        public void main(String[] args) throws Exception {
            final ClientConfig config = new ClientConfig().setBootstrapUrls(args[0]);
            final SocketStoreClientFactory factory = new SocketStoreClientFactory(config);
            final StoreClient<String, String> client = factory.getStoreClient("sets");
            
            client.get("evens"); // set doesn't exist, returns null
            
            client.put("evens", "ADD 2"); // adds 2 to the set
            
            client.get("evens"); // returns "2"
            
            client.put("evens", "ADD 2, 4, 6, 8"); // adds 4, 6, and 8 to the set
            
            client.get("evens"); // returns "2,4,6,8"
            
            client.put("evens", "REM 2"); // removes 2 from the set
            
            client.get("evens"); // returns "4,6,8"
            
            client.put("evens", "REM 4, 6"); // removes 4 and 6 from the set
            
            client.get("evens"); // returns "8"
        }
    }


License
-------

Copyright (c) 2009 Coda Hale, licensed to all under the terms of the MIT License.