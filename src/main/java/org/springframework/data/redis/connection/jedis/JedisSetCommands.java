/*
 * Copyright 2017-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.redis.connection.jedis;

import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.MultiKeyPipelineBase;
import redis.clients.jedis.ScanParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.connection.RedisSetCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.KeyBoundCursor;
import org.springframework.data.redis.core.ScanIteration;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.util.Assert;

/**
 * @author Christoph Strobl
 * @author Mark Paluch
 * @since 2.0
 */
class JedisSetCommands implements RedisSetCommands {

	private final JedisConnection connection;

	JedisSetCommands(JedisConnection connection) {
		this.connection = connection;
	}

	@Override
	public Long sAdd(byte[] key, byte[]... values) {

		Assert.notNull(key, "Key must not be null!");
		Assert.notNull(values, "Values must not be null!");
		Assert.noNullElements(values, "Values must not contain null elements!");

		return connection.invoke().just(BinaryJedis::sadd, MultiKeyPipelineBase::sadd, key, values);
	}

	@Override
	public Long sCard(byte[] key) {

		Assert.notNull(key, "Key must not be null!");

		return connection.invoke().just(BinaryJedis::scard, MultiKeyPipelineBase::scard, key);
	}

	@Override
	public Set<byte[]> sDiff(byte[]... keys) {

		Assert.notNull(keys, "Keys must not be null!");
		Assert.noNullElements(keys, "Keys must not contain null elements!");

		return connection.invoke().just(BinaryJedis::sdiff, MultiKeyPipelineBase::sdiff, keys);
	}

	@Override
	public Long sDiffStore(byte[] destKey, byte[]... keys) {

		Assert.notNull(destKey, "Destination key must not be null!");
		Assert.notNull(keys, "Source keys must not be null!");
		Assert.noNullElements(keys, "Source keys must not contain null elements!");

		return connection.invoke().just(BinaryJedis::sdiffstore, MultiKeyPipelineBase::sdiffstore, destKey, keys);
	}

	@Override
	public Set<byte[]> sInter(byte[]... keys) {

		Assert.notNull(keys, "Keys must not be null!");
		Assert.noNullElements(keys, "Keys must not contain null elements!");

		return connection.invoke().just(BinaryJedis::sinter, MultiKeyPipelineBase::sinter, keys);
	}

	@Override
	public Long sInterStore(byte[] destKey, byte[]... keys) {

		Assert.notNull(destKey, "Destination key must not be null!");
		Assert.notNull(keys, "Source keys must not be null!");
		Assert.noNullElements(keys, "Source keys must not contain null elements!");

		return connection.invoke().just(BinaryJedis::sinterstore, MultiKeyPipelineBase::sinterstore, destKey, keys);
	}

	@Override
	public Boolean sIsMember(byte[] key, byte[] value) {

		Assert.notNull(key, "Key must not be null!");
		Assert.notNull(value, "Value must not be null!");

		return connection.invoke().just(BinaryJedis::sismember, MultiKeyPipelineBase::sismember, key, value);
	}

	@Override
	public List<Boolean> sMIsMember(byte[] key, byte[]... values) {

		Assert.notNull(key, "Key must not be null!");
		Assert.notNull(values, "Values must not be null!");
		Assert.noNullElements(values, "Values must not contain null elements!");

		return connection.invoke().just(BinaryJedis::smismember, MultiKeyPipelineBase::smismember, key, values);
	}

	@Override
	public Set<byte[]> sMembers(byte[] key) {

		Assert.notNull(key, "Key must not be null!");

		return connection.invoke().just(BinaryJedis::smembers, MultiKeyPipelineBase::smembers, key);
	}

	@Override
	public Boolean sMove(byte[] srcKey, byte[] destKey, byte[] value) {

		Assert.notNull(srcKey, "Source key must not be null!");
		Assert.notNull(destKey, "Destination key must not be null!");
		Assert.notNull(value, "Value must not be null!");

		return connection.invoke().from(BinaryJedis::smove, MultiKeyPipelineBase::smove, srcKey, destKey, value)
				.get(JedisConverters::toBoolean);
	}

	@Override
	public byte[] sPop(byte[] key) {

		Assert.notNull(key, "Key must not be null!");

		return connection.invoke().just(BinaryJedis::spop, MultiKeyPipelineBase::spop, key);
	}

	@Override
	public List<byte[]> sPop(byte[] key, long count) {

		Assert.notNull(key, "Key must not be null!");

		return connection.invoke().from(BinaryJedis::spop, MultiKeyPipelineBase::spop, key, count).get(ArrayList::new);
	}

	@Override
	public byte[] sRandMember(byte[] key) {

		Assert.notNull(key, "Key must not be null!");

		return connection.invoke().just(BinaryJedis::srandmember, MultiKeyPipelineBase::srandmember, key);
	}

	@Override
	public List<byte[]> sRandMember(byte[] key, long count) {

		Assert.notNull(key, "Key must not be null!");

		if (count > Integer.MAX_VALUE) {
			throw new IllegalArgumentException("Count must be less than Integer.MAX_VALUE for sRandMember in Jedis.");
		}

		return connection.invoke().just(BinaryJedis::srandmember, MultiKeyPipelineBase::srandmember, key, (int) count);
	}

	@Override
	public Long sRem(byte[] key, byte[]... values) {

		Assert.notNull(key, "Key must not be null!");
		Assert.notNull(values, "Values must not be null!");
		Assert.noNullElements(values, "Values must not contain null elements!");

		return connection.invoke().just(BinaryJedis::srem, MultiKeyPipelineBase::srem, key, values);
	}

	@Override
	public Set<byte[]> sUnion(byte[]... keys) {

		Assert.notNull(keys, "Keys must not be null!");
		Assert.noNullElements(keys, "Keys must not contain null elements!");

		return connection.invoke().just(BinaryJedis::sunion, MultiKeyPipelineBase::sunion, keys);
	}

	@Override
	public Long sUnionStore(byte[] destKey, byte[]... keys) {

		Assert.notNull(destKey, "Destination key must not be null!");
		Assert.notNull(keys, "Source keys must not be null!");
		Assert.noNullElements(keys, "Source keys must not contain null elements!");

		return connection.invoke().just(BinaryJedis::sunionstore, MultiKeyPipelineBase::sunionstore, destKey, keys);
	}

	@Override
	public Cursor<byte[]> sScan(byte[] key, ScanOptions options) {
		return sScan(key, 0, options);
	}

	/**
	 * @since 1.4
	 * @param key
	 * @param cursorId
	 * @param options
	 * @return
	 */
	public Cursor<byte[]> sScan(byte[] key, long cursorId, ScanOptions options) {

		Assert.notNull(key, "Key must not be null!");

		return new KeyBoundCursor<byte[]>(key, cursorId, options) {

			@Override
			protected ScanIteration<byte[]> doScan(byte[] key, long cursorId, ScanOptions options) {

				if (isQueueing() || isPipelined()) {
					throw new UnsupportedOperationException("'SSCAN' cannot be called in pipeline / transaction mode.");
				}

				ScanParams params = JedisConverters.toScanParams(options);

				redis.clients.jedis.ScanResult<byte[]> result = connection.getJedis().sscan(key,
						JedisConverters.toBytes(cursorId), params);
				return new ScanIteration<>(Long.valueOf(result.getCursor()), result.getResult());
			}

			protected void doClose() {
				JedisSetCommands.this.connection.close();
			};
		}.open();
	}

	private boolean isPipelined() {
		return connection.isPipelined();
	}

	private boolean isQueueing() {
		return connection.isQueueing();
	}

}
