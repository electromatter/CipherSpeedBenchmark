/*
 * A simple benchmark like openssl speed but only for aes-cbc
 *
 * Copyright (c) 2017, Eric Chai <electromatter@gmail.com>
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package info.electromatter.cipherbenchmark;

import java.util.concurrent.TimeUnit;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class CipherSpeedBenchmark {

    private static final int DURATION = 3;

    public static void main(String[] args) throws Exception {
        int[] sizes = {16, 64, 256, 1024, 8192};
        int[] keysizes = {128, 192, 256};
        String[] modes = {"AES/CBC/NoPadding"};
        Timer timer = new Timer(DURATION, TimeUnit.SECONDS);

        for (String mode : modes) {
            for (int keysize : keysizes) {

                for (int blocksize : sizes) {
                    Cipher cipher = Cipher.getInstance(mode);
                    KeyGenerator keygen = KeyGenerator.getInstance("AES");
                    keygen.init(keysize);
                    SecretKey key = keygen.generateKey();

                    long count = 0;
                    byte[] data = new byte[blocksize];

                    System.out.printf("Doing %s (%d bits) for %d on %d size blocks: ", mode, keysize, DURATION, blocksize);
                    cipher.init(Cipher.ENCRYPT_MODE, key);
                    for (timer.resetTimer(); !timer.timerExpired(); count++) {
                        cipher.doFinal(data);
                    }

                    double elapsed = timer.getElapsed(TimeUnit.SECONDS);
                    System.out.println(String.format("%d in %.2fs (%.2fkB/s)", count, elapsed, count * blocksize / elapsed / 1000.));
                }
            }
        }
    }

}
