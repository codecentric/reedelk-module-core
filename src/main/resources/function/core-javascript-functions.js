// Util
var Util = {};

(function(util) {

    /**
     * Returns a tmp dir on the file system.
     */
    this.tmpdir = function() {
        return util.tmpdir();
    };

    /**
     * Returns a unique UUID.
     * @returns {*}
     */
    this.uuid = function() {
        return util.uuid();
    };


    /**
     * Merge the contents of two or more objects together into a new object.
     * If the optional argument deep is true, the merge becomes recursive (aka. deep copy).
     */
    this.merge = function () {
        // Variables
        var extended = {};
        var deep = false;
        var i = 0;

        // Check if a deep merge
        if (typeof (arguments[0]) === 'boolean') {
            deep = arguments[0];
            i++;
        }

        // Merge the object into the extended object
        var extend = function (obj) {
            for (var prop in obj) {
                // If the object has 'hasOwnProperty' we use it, otherwise the object
                // is a Java Map type, therefore we don't use it.
                if (obj.hasOwnProperty ? obj.hasOwnProperty(prop): true) {
                    if (deep && Object.prototype.toString.call(obj[prop]) === '[object Object]') {
                        // If we're doing a deep merge and the property is an object
                        extended[prop] = Util.merge(true, extended[prop], obj[prop]);
                    } else {
                        // Otherwise, do a regular merge
                        extended[prop] = obj[prop];
                    }
                }
            }
        };

        // Loop through each object and conduct a merge
        for (; i < arguments.length; i++) {
            extend(arguments[i]);
        }

        return extended;
    };

    /**
     * Groups the object’s values by a criterion.
     * Pass either a string attribute to group by, or a function that returns the criterion.
     */
    this.groupBy = function (arr, criteria) {

        // When we call this function with a Java object, the 'arr' argument is an object
        // when its Java type is collection. Therefore we need to convert the Java object
        // to an array.
        var realArray;

        var objectPrototype = Object.prototype.toString.call(arr);

        if (objectPrototype === '[object Array]') {
            realArray = arr;
        } else if (objectPrototype === '[object java.util.HashSet]') {
            var setAsArray = arr.toArray();
            realArray = [];
            for (var p in setAsArray) {
                realArray.push(setAsArray[p]);
            }
        } else {
            realArray = [];
            for (var p in arr) {
                realArray.push(arr[p]);
            }
        }

        return realArray.reduce(function (obj, item) {
            // Check if the criteria is a function to run on the item or a property of it
            var key = typeof criteria === 'function' ? criteria(item) : item[criteria];

            // If the key doesn't exist yet, create it
            if (!obj.hasOwnProperty(key)) {
                obj[key] = [];
            }

            // Push the value to the object
            obj[key].push(item);

            // Return the object to the next item in the loop
            return obj;

        }, {});
    }

}).call(Util, util);
