package com.mz.json.examples;

import com.mz.json.examples.model.base.ResponseError;
import com.mz.json.examples.model.base.ResponseErrors;
import com.mz.json.examples.model.complex.ComplexResponse;
import com.mz.json.examples.model.complex.CustomData;
import com.mz.json.examples.model.complex.Shard;
import com.mz.json.examples.model.complex.Shards;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static com.mz.json.helpers.ModelCoDec.*;
import static java.lang.System.currentTimeMillis;

public class PerformanceTest {
    private static Random random = new Random();
    private static long oldTime = 0L;

    @Test
    public void test() throws Exception {
        ComplexResponse response;
        String json;

        System.out.println("Validation | Size  | Generate | Encode | Decode");
        getDuration();
        for(boolean validation : Arrays.asList(false, true)) {
            for (int size : Arrays.asList(100, 1000, 10000)) {
                response = generateResponse(size, validation);
                final long generation = getDuration();

                setValidateModels(validation);
                json = encodeModel(response);
                final long encoding = getDuration();
                decodeModel(json, ComplexResponse.class);
                final long decoding = getDuration();

                System.out.println(String.format("%10b | %5d | %8d | %6d | %6d",
                        validation, size, generation, encoding, decoding));
            }
        }
    }

    private static long getDuration() {
        long newTime = currentTimeMillis();
        long duration = newTime - oldTime;
        oldTime = newTime;
        return duration;
    }

    private static ComplexResponse generateResponse(int size, boolean flag) {
        int iFlag = flag ? 1 : 0;

        ComplexResponse response = new ComplexResponse();
        response.customData = new CustomData(-1);
        response.shards = new Shards();
        response.errors = new ResponseErrors();

        for (int i = 0; i < size; i++) {
            final Shard shard = new Shard();
            shard.recordsUpdated = random.nextInt(10000) + 1;
            shard.commitDate = String.format("day-#%d-%d", random.nextInt(31) + 1, iFlag);
            shard.replicated = true;
            shard.failed = false;
            shard.mixed = (i > 0)
                    ? response.shards.get(String.format("key-#%d-%d", random.nextInt(i), iFlag))
                    : null;

            response.shards.put(String.format("key-#%d-%d", i, iFlag), shard);
        }

        for (int i = 0; i < size; i++) {
            final ResponseError error = new ResponseError();
            error.code = random.nextInt(600) + 1;
            error.reason = String.format("error-#%d-%d", i, iFlag);
            response.errors.add(error);
        }

        return response;
    }
}
