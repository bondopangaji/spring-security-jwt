/*
 * Copyright (c) 2022 Bondo Pangaji
 *
 * This software is released under the MIT License.
 * https://opensource.org/licenses/MIT
 */

package io.bondopangaji.springsecurityjwt.error;

import lombok.experimental.StandardException;

/**
 * Custom exception.
 * @see StandardException
 * @author Bondo Pangaji
 */
@StandardException
public class CustomException extends RuntimeException{
    public CustomException(String message) {
        super(message);
    }
}