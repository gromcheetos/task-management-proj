package org.app.controllers.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.nio.ByteBuffer;
import java.util.UUID;

@Converter
public class UUIDConverter implements AttributeConverter<UUID, byte[]> {
    @Override
    public byte[] convertToDatabaseColumn(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        String[] uuidV1Parts = uuid.toString().split("-");
        String sequentialUUID = uuidV1Parts[2]+uuidV1Parts[1]+uuidV1Parts[0]+uuidV1Parts[3]+uuidV1Parts[4];

        String sequentialUuidV1 = String.join("", sequentialUUID);
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(Long.parseUnsignedLong(sequentialUuidV1.substring(0, 16), 16));
        bb.putLong(Long.parseUnsignedLong(sequentialUuidV1.substring(16), 16));

        return bb.array();
    }

    @Override
    public UUID convertToEntityAttribute(byte[] dbData) {
        if (dbData == null) {
            return null;
        }
        // BINARY(16) 데이터를 다시 UUID로 변환
        ByteBuffer bb = ByteBuffer.wrap(dbData);
        long high = bb.getLong();
        long low = bb.getLong();
        return new UUID(high, low);
    }
}
