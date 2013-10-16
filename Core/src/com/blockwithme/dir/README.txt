We have bundles.
Bundles have Maven versions.
Bundles have packages.
Packages have classes.
Classes have fields.

So a bundle string id is "bundle-name . version"
And a class string id is "bundle-name . version . package . class-name"
And a field string id is "bundle-name . version . package . class-name . field"

A bundle set is just the (sorted) concatenated list of bundle string IDs.

We assume no class is in multiple different bundles.

At the top directory, we have something like this:

* "classes" ==>
    <bundle-name . version . package . class> ==>
        <field> ==> long ID
        "this" ==>
            <schema> ==> * (exactly one child/schema per class)
* "bundle-set" ==>
    <sorted concatenated list of bundle string IDs> ==> long ID

With this primitive database, we can store all the string, and have them
converted to long, which we can use when serializing. It is important to
think about the scope of unity. Bundle-sets have their own global scope.
All classes from all bundles have a global scope.
And the fields, if we need their ID, have the class as scope.
Schema of class is a bit hard to get, because we need a child just for it.

Remember that data from multiple bundles can be serialized together.
Therefore we have bundle-sets. And that gives us one ID per set.
This ID decides on the MessagePack instance to use.
And that instance must know about all the classes from all the bundles of the set.
But there is the issue that the class might not exist anymore.

Within one single class, the field IDs could be 1, 2, 3, ...
so the values are small and can be optimized by MessagePack

We can't really do that for classes. If the IDs are really long, there is the
option, as is done in Java serialisation, that we have for each class a
byte-stream local index, which is the order of definition of class. So we use
the ID only the first time a class is used, and afterward it's index. Index
should be small, so optimizable. Need to know if new class or not. Maybe
use 0 as class ID for know class (0 gets optimized, I assume)?

Finally, some of the serialisation-related stuff I wrote is in BlowenSer.
I think it is still checked in, in the private repo.

Hope this helps a bit.
