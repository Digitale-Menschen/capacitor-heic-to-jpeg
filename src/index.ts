import { registerPlugin } from '@capacitor/core';

import type { CapacitorHeicToJpegPlugin } from './definitions';

const CapacitorHeicToJpeg = registerPlugin<CapacitorHeicToJpegPlugin>(
  'CapacitorHeicToJpeg',
  {
    web: () => import('./web').then(m => new m.CapacitorHeicToJpegWeb()),
  },
);

export * from './definitions';
export { CapacitorHeicToJpeg };
