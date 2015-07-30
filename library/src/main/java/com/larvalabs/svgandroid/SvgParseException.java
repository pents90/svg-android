package com.larvalabs.svgandroid;

/**
 * Runtime exception thrown when there is a problem parsing an SVG.
 *
 * @author Larva Labs, LLC
 */
public class SvgParseException extends RuntimeException {

    public SvgParseException(String s) {
        super(s);
    }

    public SvgParseException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SvgParseException(Throwable throwable) {
        super(throwable);
    }

}