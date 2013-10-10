package it.crinali.www.archprot.service;

import org.springframework.stereotype.Component;

public interface ThumbnailService {
	
	public byte[] generateThumbnail(byte [] pdf	);
}
