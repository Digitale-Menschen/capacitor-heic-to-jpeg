import { WebPlugin } from '@capacitor/core';

import type { CapacitorHeicToJpegPlugin } from './definitions';

export class CapacitorHeicToJpegWeb
  extends WebPlugin
  implements CapacitorHeicToJpegPlugin {
  async convertToJpeg(options: { path: string }): Promise<{ path: string }> {
    console.log('convertToJpeg is not available on web', options);
    return options;
  }
}
