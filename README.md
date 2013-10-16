LessObjects
===========

A Java API that allows developers to represent C-style bit-field structs and unions.

While working on the VoxelEditorModel, I once again, for the thousandth time, wished there was an API that allowed us to represent C-style bit-field structs and unions.

LessObjects is that, and some more. The main use case, is when you have 100K to millions of objects, which you want to keep track of, in the form of an (possibly multi-dimentional) array, but would like to save yourself the "object overhead". That is, you want to keep the *data* of those objects in ram, while no actually having those countless small objects slowing down the GC. In 64bit Java, Objects ordinarily have a overhead of 16 bytes, plus a 8 byte reference to keep track of them. That overhead of 24 byte disappear when using LessObjects. This is for simple oject that are all of the same type, and do not use special features, like optional fields ... There are multiple ways to store the data internally (more to come), as well as support for transactions, and "differences" (record changed data, and send it to a remote location, to keep data in sync).

The initial version is aimed at offering all the most important features; future versions will aim to provide other type of internal storage, with difference characteristics, and generally making the code faster.

The project is currently split between "core", where the API and it's implementation is, "proxy", which allows the automatic creation of a "Struct" for Object interfaces, offering a user-friendly bean-like interface, and benchmarks.
