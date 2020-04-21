package ru.crg.reglaments.batchprocessing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import ru.crg.reglaments.entity.ReglamentLink;

public class ReglamentLinkProcessor implements ItemProcessor<ReglamentLink, ReglamentLink> {

    private static final Logger log = LoggerFactory.getLogger(ReglamentLinkProcessor.class);

    @Override
    public ReglamentLink process(ReglamentLink reglamentLink) throws Exception {
        log.info("Link processor do... ");

        return reglamentLink;
    }
}
