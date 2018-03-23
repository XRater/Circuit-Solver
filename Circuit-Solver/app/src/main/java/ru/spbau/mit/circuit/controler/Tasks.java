package ru.spbau.mit.circuit.controler;


import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.spbau.mit.circuit.model.Model;
import ru.spbau.mit.circuit.storage.Converter;
import ru.spbau.mit.circuit.storage.StorageException;

/**
 * This class stores some tasks to work with converter. Every task is AsyncTask which will be called
 * in another thread, therefore MainActivity thread will not be paused.
 * <p>
 * Class has number of inner tasks and and static factory methods to create them.
 * <p>
 * Every task requires converter and mode to work with.
 */
class Tasks {

    /**
     * The method takes model as an argument.
     * Returned task saves circuit, represented by model, to the storage.
     * Requires target file name to execute.
     */
    static SaveTask saveTask(Converter.Mode mode, Converter converter, Model model) {
        return new SaveTask(mode, converter, model);
    }

    /**
     * Returned task returns all saved circuits from the storage.
     */
    static GetCircuitsTask getCircuitsTask(Converter.Mode mode, Converter converter) {
        return new GetCircuitsTask(mode, converter);
    }

    /**
     * Returned task loads circuit from the storage.
     * Requires target file name to execute.
     */
    static LoadTask loadTask(Converter.Mode mode, Converter converter) {
        return new LoadTask(mode, converter);
    }

    /**
     * Returned task deletes circuit from the storage.
     * Requires target filename to execute.
     */
    static DeleteTask deleteTask(Converter.Mode mode, Converter converter) {
        return new DeleteTask(mode, converter);
    }

    static class SaveTask extends AsyncTask<String, Void, ResultHolder<Boolean>> {
        private final Converter.Mode mode;
        private final Converter converter;
        private final Model model;

        SaveTask(Converter.Mode mode, Converter converter, Model model) {
            this.converter = converter;
            this.mode = mode;
            this.model = model;
        }

        @NonNull
        @Override
        public ResultHolder<Boolean> doInBackground(String... filename) {
            try {
                return new ResultHolder<>(converter.save(mode, model, filename[0]));
            } catch (StorageException e) {
                return new ResultHolder<>(e);
            }
        }
    }

    static class GetCircuitsTask extends AsyncTask<Void, Void, List<String>> {

        private final Converter converter;
        private final Converter.Mode mode;

        private GetCircuitsTask(Converter.Mode mode, Converter converter) {
            this.mode = mode;
            this.converter = converter;
        }

        @NonNull
        @Override
        public List<String> doInBackground(Void... voids) {
            return converter.getCircuits(mode);
        }
    }

    static class LoadTask extends AsyncTask<String, Void, ResultHolder<Model>> {

        private final Converter.Mode mode;
        private final Converter converter;

        private LoadTask(Converter.Mode mode, Converter converter) {
            this.mode = mode;
            this.converter = converter;
        }

        @Nullable
        @Override
        public ResultHolder<Model> doInBackground(@NonNull String... filename) {
            if (filename.length != 1) {
                throw new IllegalArgumentException();
            }
            try {
                return new ResultHolder<>(converter.load(mode, filename[0]));
            } catch (StorageException e) {
                return new ResultHolder<>(e);
            }
        }
    }

    static class DeleteTask extends AsyncTask<String, Void, ResultHolder<Boolean>> {

        private final Converter.Mode mode;
        private final Converter converter;

        private DeleteTask(Converter.Mode mode, Converter converter) {
            this.mode = mode;
            this.converter = converter;
        }

        @NonNull
        @Override
        public ResultHolder<Boolean> doInBackground(String... filename) {
            try {
                return new ResultHolder<>(converter.delete(mode, filename[0]));
            } catch (StorageException e) {
                return new ResultHolder<>(e);
            }
        }
    }

    static class ResultHolder<R> {

        private R result;
        @Nullable
        private StorageException exception = null;

        private ResultHolder(R result) {
            this.result = result;
        }

        private ResultHolder(@Nullable StorageException exception) {
            this.exception = exception;
        }

        R getResult() throws StorageException {
            if (exception != null) {
                throw exception;
            }

            return result;
        }
    }
}
