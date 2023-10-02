export interface CapacitorHeicToJpegPlugin {
  convertToJpeg(options: { path: string }): Promise<{ path: string }>;
}
