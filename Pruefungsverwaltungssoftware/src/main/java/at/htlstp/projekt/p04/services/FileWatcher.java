package at.htlstp.projekt.p04.services;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import at.htlstp.projekt.p04.graphic_tools.Utilities;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

/**
 *
 * @author Dru
 */
public class FileWatcher extends Task<ObservableList<String>> {

    private final Path path;
    private final List<String> items = new ArrayList<>();
    private WatchService watcher;

    public FileWatcher(Path path) {
        this.path = path;
    }

    @Override
    protected ObservableList<String> call() throws Exception {

        for (Path p : Files.list(path).collect(Collectors.toList())) {
            items.add(p.getFileName().toString());
        }
        updateValue(FXCollections.observableArrayList(items));
        try {

            watcher = FileSystems.getDefault().newWatchService();
            path.register(watcher, ENTRY_CREATE, ENTRY_DELETE);
            while (true) {
                WatchKey key = null;
                try {
                    key = watcher.take();
                } catch (ClosedWatchServiceException e) {
                    System.out.println("Watchservice beendet!");
                    return null;
                } catch (InterruptedException ex) {
                    System.out.println(ex);
                    return null;
                }

                key.pollEvents().forEach((event) -> {
                    try {
                        WatchEvent.Kind<?> kind = event.kind();
                        WatchEvent<Path> ev = (WatchEvent<Path>) event;
                        Path fileName = ev.context();
                        System.out.println(kind.name() + ": " + fileName);
                        switch (kind.name()) {
                            case "ENTRY_CREATE":
                                items.add(fileName.toString());
                                break;
                            case "ENTRY_DELETE":
                                items.remove(fileName.toString());
                                break;
                        }
                        updateValue(FXCollections.observableArrayList(items));

                    } catch (Exception e) {
                        Utilities.showMessageForExceptions(e, null, false);
                    }
                    
                });
                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }

        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }

    public void stopWatcher() {
        try {
            if (watcher != null) {
                this.watcher.close();
            }
        } catch (IOException ex) {
            Utilities.showMessageForExceptions(ex, null, true);
        }
    }

}
