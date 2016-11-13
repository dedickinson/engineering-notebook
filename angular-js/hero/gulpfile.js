"use strict";

var gulp = require("gulp");
var browserify = require("browserify");
var source = require("vinyl-source-stream");
var tsify = require("tsify");
var uglify = require("gulp-uglify");
var sourcemaps = require("gulp-sourcemaps");
var buffer = require("vinyl-buffer");

var paths = {
    pages: ["*.html", "*.css"]
};

var config = {
    dest: "./dist",
    root: "./"
};

gulp.task("help", $.taskListing);
gulp.task("default", ["help"]);

gulp.task("clean", function (done) {
    log("clean");
    del([config.index.run]).then(paths => {
        // console.log("Deleted files and folders:\n", paths.join("\n"));
        done();
    });
});

gulp.task("copy-html", function () {
    return gulp.src(paths.pages)
        .pipe(gulp.dest(output_dir));
});

gulp.task("default", ["copy-html"], function () {

});

module.exports = gulp;